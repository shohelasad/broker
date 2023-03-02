package com.lab.broker.service;

import com.lab.broker.dto.BidResponseDto;

import java.util.List;
import java.util.Map;

public interface BidderService {
    List<BidResponseDto> sendBidRequests(Long id, Map<String, String> params);
}
