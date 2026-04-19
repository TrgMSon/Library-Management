package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ReturnBookDTO;
import com.example.demo.service.ReturnService;

@RestController
@RequestMapping("/api/borrowCard")
@CrossOrigin(origins = "*")
public class ReturnController {
    
    @Autowired
    private ReturnService returnService;
    
    @PostMapping("/returnBook")
    public ResponseEntity<Map<String, Object>> returnBook(@RequestBody ReturnBookDTO returnBookDTO) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = returnService.processReturn(
                returnBookDTO.getBorrowCardId(),
                returnBookDTO.getBookId(),
                returnBookDTO.getReturnDate(),
                returnBookDTO.getFine()
            );
            
            response.put("success", success);
            response.put("message", success ? "Trả sách thành công" : "Trả sách thất bại");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
}
