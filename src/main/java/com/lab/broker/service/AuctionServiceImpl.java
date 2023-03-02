package com.lab.broker.service;

import com.lab.broker.dto.AuctionResponseDto;
import com.lab.broker.dto.BidResponseDto;
import com.lab.broker.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class AuctionServiceImpl implements  AuctionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuctionServiceImpl.class);

    @Override
    public AuctionResponseDto generateAuctionResponse(List<BidResponseDto> bidResponses) {
        if (bidResponses.isEmpty()) {
            LOGGER.error("No bids were received for the auction.");
            throw new ResourceNotFoundException("No bids were received for the auction.");
        }

        BidResponseDto highestBidder = Collections.max(bidResponses, Comparator.comparingLong(BidResponseDto::getBid));
        String content = highestBidder.getContent().replaceAll("\\$price\\$", highestBidder.getBid().toString());
        AuctionResponseDto responseDto = new AuctionResponseDto();
        responseDto.setContent(content);

        return responseDto;
    }
}
