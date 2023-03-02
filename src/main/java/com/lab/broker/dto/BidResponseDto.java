package com.lab.broker.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidResponseDto {
    private Long id;
    private Long bid;
    private String content;
}
