package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateDetailCardDTO {
    private int borrowCardId;
    private int bookId;
    private String expire;
}
