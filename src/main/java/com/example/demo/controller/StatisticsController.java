package com.example.demo.controller;

import com.example.demo.dto.BookStatisticsDTO;
import com.example.demo.dto.ReaderStatisticsDTO;
import com.example.demo.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {
    
    @Autowired
    private StatisticsService statisticsService;
    
    // Hiển thị trang thống kê
    @GetMapping
    public String statisticsPage() {
        return "statistics/index";
    }
    
    // Hiển thị form thống kê sách
    @GetMapping("/books")
    public String bookStatisticsForm(Model model) {
        model.addAttribute("type", "books");
        return "statistics/form";
    }
    
    // Xử lý thống kê sách
    @PostMapping("/books")
    public String getBookStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Model model) {
        
        if (!statisticsService.isValidDateRange(startDate, endDate)) {
            model.addAttribute("error", "Khoảng thời gian không hợp lệ!");
            model.addAttribute("type", "books");
            return "statistics/form";
        }
        
        if (!statisticsService.hasDataInDateRange(startDate, endDate)) {
            model.addAttribute("error", "Không có dữ liệu trong khoảng thời gian này!");
            model.addAttribute("type", "books");
            return "statistics/form";
        }
        
        List<BookStatisticsDTO> statistics = statisticsService.getMostBorrowedBooks(startDate, endDate);
        model.addAttribute("statistics", statistics);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("type", "books");
        return "statistics/result";
    }
    
    // Hiển thị form thống kê độc giả
    @GetMapping("/readers")
    public String readerStatisticsForm(Model model) {
        model.addAttribute("type", "readers");
        return "statistics/form";
    }
    
    // Xử lý thống kê độc giả
    @PostMapping("/readers")
    public String getReaderStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Model model) {
        
        if (!statisticsService.isValidDateRange(startDate, endDate)) {
            model.addAttribute("error", "Khoảng thời gian không hợp lệ!");
            model.addAttribute("type", "readers");
            return "statistics/form";
        }
        
        if (!statisticsService.hasDataInDateRange(startDate, endDate)) {
            model.addAttribute("error", "Không có dữ liệu trong khoảng thời gian này!");
            model.addAttribute("type", "readers");
            return "statistics/form";
        }
        
        List<ReaderStatisticsDTO> statistics = statisticsService.getMostActiveReaders(startDate, endDate);
        model.addAttribute("statistics", statistics);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("type", "readers");
        return "statistics/result";
    }
}