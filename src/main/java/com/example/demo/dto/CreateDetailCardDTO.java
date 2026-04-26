package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateDetailCardDTO {
    private int borrowCardId;
    private String bookId;
    private String expire;
}
