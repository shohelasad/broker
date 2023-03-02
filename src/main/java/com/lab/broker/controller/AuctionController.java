package com.lab.broker.controller;

import com.lab.broker.dto.AuctionResponseDto;
import com.lab.broker.dto.BidResponseDto;
import com.lab.broker.exception.BadRequestException;
import com.lab.broker.service.AuctionServiceImpl;
import com.lab.broker.service.BidderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AuctionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuctionController.class);

    private final AuctionServiceImpl auctionService;
    private final BidderServiceImpl bidderService;

    @Autowired
    public AuctionController(AuctionServiceImpl auctionService, BidderServiceImpl bidderService) {
        this.auctionService = auctionService;
        this.bidderService = bidderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getAuction(@PathVariable Long id, @RequestParam Map<String, String> params) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue().isEmpty()) {
                LOGGER.error("Query parameter '{}' has an empty value.", entry.getKey());
                throw new BadRequestException(String.format("Query parameter '%s' has an empty value.", entry.getKey()));
            }
        }

        LOGGER.info("Sending auction request to bidders");
        List<BidResponseDto> bidResponses = bidderService.sendBidRequests(id, params);
        AuctionResponseDto response = auctionService.generateAuctionResponse(bidResponses);

        return ResponseEntity.ok(response.getContent());
    }
}