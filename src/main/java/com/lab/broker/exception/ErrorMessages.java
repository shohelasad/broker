package com.lab.broker.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessages {
    private int statusCode;
    private LocalDateTime timestamp;
    private String message;
    private String description;
}
