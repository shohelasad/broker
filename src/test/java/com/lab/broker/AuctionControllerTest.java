package com.lab.broker;


import com.lab.broker.controller.AuctionController;
import com.lab.broker.dto.AuctionResponseDto;
import com.lab.broker.dto.BidResponseDto;
import com.lab.broker.service.AuctionServiceImpl;
import com.lab.broker.service.BidderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.ArgumentMatchers.any;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuctionController.class)
class AuctionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuctionServiceImpl auctionService;
    @MockBean
    private BidderServiceImpl bidderService;

    @Test
    void testGetAuction() throws Exception {
        String content = "a:300";
        Long auctionId = 1L;
        List<BidResponseDto> responses = new ArrayList<>();
        BidResponseDto responseDto = new BidResponseDto();
        responseDto.setId(1L);
        responseDto.setBid(100L);
        responseDto.setContent("a:$price$");
        responses.add(responseDto);
        responseDto = new BidResponseDto();
        responseDto.setId(2L);
        responseDto.setBid(200L);
        responseDto.setContent("b:$price$");
        responses.add(responseDto);
        responseDto = new BidResponseDto();
        responseDto.setId(3L);
        responseDto.setBid(300L);
        responseDto.setContent("c:$price$");
        responses.add(responseDto);
        AuctionResponseDto expectedAuctionResponse = new AuctionResponseDto();
        expectedAuctionResponse.setContent(content);

        // Mock the sendBidRequests method to return the expected BidResponseDto objects
        when(bidderService.sendBidRequests(any(Long.class), any(Map.class))).thenReturn(responses);
        when(auctionService.generateAuctionResponse(any(List.class))).thenReturn(expectedAuctionResponse);

        // Perform the request
        MvcResult mvcResult = mockMvc.perform(get("/" + auctionId).param("b", "5").param("c", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();

        // Assert that the response is not null and contains the expected content
        Assert.assertNotNull(responseContent);
        Assert.assertTrue(responseContent.contains(content));
    }

    @Test
    public void getAuction_withEmptyParams_throwsBadRequestException() throws Exception {
        Long auctionId = 1L;

        MvcResult mvcResult = mockMvc.perform(get("/" + auctionId).param("b", "").param("c", "10"))
                .andExpect(status().isBadRequest()).andReturn();

        Mockito.verify(bidderService, never()).sendBidRequests(Mockito.anyLong(), Mockito.anyMap());
        Mockito.verify(auctionService, never()).generateAuctionResponse(Mockito.anyList());
    }

}
