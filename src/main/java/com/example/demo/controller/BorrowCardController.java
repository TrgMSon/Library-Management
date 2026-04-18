package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BorrowCardDTO;
import com.example.demo.dto.BorrowCardDetailDTO;
import com.example.demo.service.BorrowCardService;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/borrowCard")
public class BorrowCardController {
    @Autowired
    private BorrowCardService borrowCardService;

    @GetMapping("/getAllCard")
    public ArrayList<BorrowCardDTO> getAllCard() {
        return borrowCardService.getAllBorrowCard();
    }

    @GetMapping("/getDetailCard")
    public BorrowCardDetailDTO getDetailCard(@RequestParam int cardId) {
        return borrowCardService.getDetailCard(cardId);
    }
    
    @GetMapping("/searchCard")
    public ArrayList<BorrowCardDTO> getMethodName(@RequestParam String target, @RequestParam String option) {
        return borrowCardService.searchCard(target, option);
    }
    
}
