package com.example.demo.controller;

import com.example.demo.dto.ReaderDTO;
import com.example.demo.model.Reader;
import com.example.demo.model.User;
import com.example.demo.repository.ReaderRepo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.CreateCardDTO;
import com.example.demo.dto.CreateCardDetailInput;
import com.example.demo.service.ReaderService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@RestController
@RequestMapping("/api/reader")
@RequiredArgsConstructor
public class ReaderRestController {
    private final ReaderService readerService;
    private final ReaderRepo readerRepo;

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

    @PostMapping("/add_reader")
    public ResponseEntity<String> processAddingReader(HttpSession session, @RequestBody ReaderDTO readerDTO) {
        User currUser = (User) session.getAttribute("loggedInUser");
        if (currUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vui lòng đăng nhập trước để sử dụng tính năng");
        }

        String name = readerDTO.getName();
        String email = readerDTO.getEmail();
        String address = readerDTO.getAddress();

        Reader reader = new Reader();
        reader.setName(name);
        reader.setEmail(email);
        reader.setAddress(address);

        readerRepo.save(reader);
        return ResponseEntity.ok().body("Thêm độc giả thành công!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reader> getReader(HttpSession session, @PathVariable int id) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return ResponseEntity.noContent().build();
        }

        Reader reader = readerRepo.findById(id).orElse(null);
        if (reader == null) {
            // Returns a 404 status with empty body
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(reader);
    }

    @PostMapping("/update_reader")
    public ResponseEntity<String> updateReader(HttpSession session, @RequestBody Reader updatedReader) {
        User currUser = (User) session.getAttribute("loggedInUser");
        if (currUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vui lòng đăng nhập trước để sử dụng tính năng");
        }

        if (!readerRepo.existsById(updatedReader.getReaderId())) {
            return ResponseEntity.notFound().build();
        }

        readerRepo.save(updatedReader);
        return ResponseEntity.ok("Cập nhật thành công!");
    }

    @GetMapping("/{id}/delete")
    public ResponseEntity<String> deleteReader(HttpSession session, @PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        User currUser = (User) session.getAttribute("loggedInUser");
        if (currUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vui lòng đăng nhập trước để sử dụng tính năng");
        }

        readerService.deleteReader(id);
        return ResponseEntity.ok().body("Xoá người dùng thành công!");
    }

}