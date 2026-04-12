package com.example.demo.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCardDTO {
    private int userId;
    private int readerId;
    private BigDecimal totalAmount;
    private String note;
}
