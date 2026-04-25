package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BorrowCardDetailDTO {
    private BorrowCardDTO borrowCardDTO;
    private ArrayList<BookInCardDTO> books;
    private BigDecimal totalAmount;
}
