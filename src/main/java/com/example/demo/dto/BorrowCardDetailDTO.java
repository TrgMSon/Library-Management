package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BorrowCardDetailDTO {
    private int borrowCardId;
    private int readerId;
    private String readerName;
    private int userId;
    private String userName;
    private LocalDateTime createdAt;
    private ArrayList<BookInCardDTO> books;
    private BigDecimal totalAmount;
}
