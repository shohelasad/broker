package com.lab.broker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class BidRequestDto {
    private Long id;
    private Map<String, String> attributes;

}