package com.example.demo.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCardDetailInput {
    private ArrayList<CreateDetailCardDTO> cardDetails;
}
