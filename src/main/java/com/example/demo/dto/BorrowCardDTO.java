package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BorrowCardDTO {
    private int borrowCardId;
    private int readerId;
    private String readerName;
    private int userId;
    private String userName;
    private LocalDateTime createdAt;
}
