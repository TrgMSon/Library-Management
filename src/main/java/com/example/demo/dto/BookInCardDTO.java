package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BookInCardDTO {
    private int bookId;
    private String bookName;
    private String status;
    private LocalDateTime expire;
    private LocalDateTime returnDate;
}
