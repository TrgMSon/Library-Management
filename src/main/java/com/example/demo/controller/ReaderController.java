package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CreateCardDTO;
import com.example.demo.dto.CreateCardDetailInput;
import com.example.demo.service.ReaderService;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/reader")
public class ReaderController {
    @Autowired
    private ReaderService readerService;
    
    @GetMapping("/checkReaderInfor")
    public boolean checkReaderInfor(@RequestParam int readerId) {
        return readerService.getReaderInfor(readerId) != null;
    }
    
    @GetMapping("/checkBorrowingBook")
    public String checkBorrowingBook(@RequestParam int readerId) {
        return readerService.checkBorrowingBook(readerId) + "";
    }
    
    @PostMapping("/createBorrowCard")
    public String createBorrowCard(@RequestBody CreateCardDTO card) {
        return readerService.createCard(card);
    }
    
    @PostMapping("/addDetailCard")
    public ArrayList<String> addDetailCard(@RequestBody CreateCardDetailInput cardDetailInput) {
        return readerService.addDetailCard(cardDetailInput.getCardDetails());
    }
    
}