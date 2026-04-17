package com.example.demo.dto;

import lombok.Data;

@Data
public class ReturnBookDTO {
    private Integer borrowCardId;
    private Integer bookId;
    private String returnDate;
    private Long fine;
    
}