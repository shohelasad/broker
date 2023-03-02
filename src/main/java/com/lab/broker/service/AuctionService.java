package com.lab.broker.service;

import com.lab.broker.dto.AuctionResponseDto;
import com.lab.broker.dto.BidResponseDto;

import java.util.List;

public interface AuctionService {

    AuctionResponseDto generateAuctionResponse(List<BidResponseDto> bidResponses);
}
