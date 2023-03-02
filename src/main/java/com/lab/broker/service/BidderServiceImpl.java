package com.lab.broker.service;

import com.lab.broker.dto.BidRequestDto;
import com.lab.broker.dto.BidResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class BidderServiceImpl implements  BidderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BidderServiceImpl.class);

    private RestTemplate restTemplate;
    private String[] bidderUrls;

    public BidderServiceImpl(RestTemplate restTemplate, @Value("${bidders}") String[] bidderUrls) {
        this.restTemplate = restTemplate;
        this.bidderUrls = bidderUrls;
    }

    @Override
    public List<BidResponseDto> sendBidRequests(Long id, Map<String, String> params) {
        List<BidResponseDto> bidResponses = new ArrayList<>();
        List<CompletableFuture<BidResponseDto>> futures = new ArrayList<>();

        for (String bidderUrl : bidderUrls) {
            BidRequestDto bidRequest = new BidRequestDto(id, params);
            CompletableFuture<BidResponseDto> future = CompletableFuture.supplyAsync(() -> {
                try {
                    ResponseEntity<BidResponseDto> responseEntity = restTemplate.postForEntity(bidderUrl, bidRequest, BidResponseDto.class);
                    return responseEntity.getBody();
                } catch (ResourceAccessException ex) {
                    LOGGER.error("Error occurred while sending bid request to " + bidderUrl + ": ", ex);
                } catch (Exception e) {
                    LOGGER.error("Unknown error occurred: " + e.getMessage());
                }

                return null;
            });
            futures.add(future);
        }

        for (CompletableFuture<BidResponseDto> future : futures) {
            try {
                BidResponseDto response = future.get();
                if (response != null) {
                    bidResponses.add(response);
                }
            } catch (InterruptedException | ExecutionException ex) {
                LOGGER.error("Error occurred while fetching bid response: ", ex);
            }
        }

        return bidResponses;
    }
}

