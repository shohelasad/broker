package com.lab.broker;

import com.lab.broker.dto.BidRequestDto;
import com.lab.broker.dto.BidResponseDto;
import com.lab.broker.service.BidderServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author asad
 */

@RunWith(SpringRunner.class)
public class BidderServiceTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BidderServiceImpl bidderService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendBidRequests() throws ExecutionException, InterruptedException {
        Long auctionId = 1L;
        Map<String, String> params = new HashMap<>();
        params.put("a", "3");
        params.put("b", "5");

        List<BidResponseDto> responses = new ArrayList<>();
        BidResponseDto responseDto1 = new BidResponseDto();
        responseDto1.setId(1L);
        responseDto1.setBid(100L);
        responseDto1.setContent("a:$price$");
        responses.add(responseDto1);
        BidResponseDto responseDto2 = new BidResponseDto();
        responseDto2.setId(2L);
        responseDto2.setBid(200L);
        responseDto2.setContent("b:$price$");
        responses.add(responseDto2);
        BidResponseDto responseDto3 = new BidResponseDto();
        responseDto3.setId(3L);
        responseDto3.setBid(300L);
        responseDto3.setContent("c:$price$");
        responses.add(responseDto3);

        RestTemplate restTemplateMock = Mockito.mock(RestTemplate.class);

        ResponseEntity<BidResponseDto> mockResponse1 = ResponseEntity.ok(responseDto1);
        Mockito.when(restTemplateMock.postForEntity(Mockito.eq("http://localhost:8081"), Mockito.any(BidRequestDto.class), Mockito.eq(BidResponseDto.class)))
                .thenReturn(mockResponse1);
        ResponseEntity<BidResponseDto> mockResponse2 = ResponseEntity.ok(responseDto2);
        Mockito.when(restTemplateMock.postForEntity(Mockito.eq("http://localhost:8082"), Mockito.any(BidRequestDto.class), Mockito.eq(BidResponseDto.class)))
                .thenReturn(mockResponse2);
        ResponseEntity<BidResponseDto> mockResponse3 = ResponseEntity.ok(responseDto3);
        Mockito.when(restTemplateMock.postForEntity(Mockito.eq("http://localhost:8083"), Mockito.any(BidRequestDto.class), Mockito.eq(BidResponseDto.class)))
                .thenReturn(mockResponse3);

        String[] bidderUrls = new String[]{"http://localhost:8081", "http://localhost:8082", "http://localhost:8083"};
        bidderService = new BidderServiceImpl(restTemplateMock, bidderUrls);
        List<BidResponseDto> actualResponses = bidderService.sendBidRequests(auctionId, params);

        Assert.assertEquals(responses.size(), actualResponses.size());
        Assert.assertEquals(responses.get(0).getBid(), actualResponses.get(0).getBid());
        Assert.assertEquals(responses.get(1).getBid(), actualResponses.get(1).getBid());
        Assert.assertEquals(responses.get(2).getBid(), actualResponses.get(2).getBid());
    }

    @Test
    public void testSendBidRequestsWithRestClientException() throws ExecutionException, InterruptedException {
        Long auctionId = 1L;
        Map<String, String> params = Map.of("a", "3", "b", "5");

        List<BidResponseDto> responses = new ArrayList<>();
        BidResponseDto responseDto = new BidResponseDto();
        responseDto.setId(1L);
        responseDto.setBid(100L);
        responseDto.setContent("a:$price$");
        responses.add(responseDto);

        RestTemplate restTemplateMock = Mockito.mock(RestTemplate.class);
        Mockito.when(restTemplateMock.postForEntity(Mockito.eq("http://localhost:8081"), Mockito.any(BidRequestDto.class), Mockito.eq(BidResponseDto.class)))
                .thenThrow(new RestClientException("Rest client exception occurred while sending bid request"));

        String[] bidderUrls = new String[]{"http://localhost:8081", "http://localhost:8082", "http://localhost:8083"};
        bidderService = new BidderServiceImpl(restTemplateMock, bidderUrls);
        List<BidResponseDto> actualResponses = bidderService.sendBidRequests(auctionId, params);

        Assert.assertEquals(0, actualResponses.size());
    }

}