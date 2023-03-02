package com.lab.broker;

import com.lab.broker.dto.AuctionResponseDto;
import com.lab.broker.dto.BidResponseDto;
import com.lab.broker.service.AuctionServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author asad
 */
@RunWith(SpringRunner.class)
public class AuctionServiceTests {

    @InjectMocks
    private AuctionServiceImpl auctionService ;

     @Test
    public void testGenerateAuctionResponse() {
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

        AuctionResponseDto result = auctionService.generateAuctionResponse(responses);
        Assert.assertEquals("c:300", result.getContent());
    }
}