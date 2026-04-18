package com.example.demo.service;

import com.example.demo.dto.BookStatisticsDTO;
import com.example.demo.dto.ReaderStatisticsDTO;
import com.example.demo.repository.BorrowCardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    
    @Autowired
    private BorrowCardRepo borrowCardRepo;
    
    // Thống kê sách mượn nhiều
    public List<BookStatisticsDTO> getMostBorrowedBooks(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = borrowCardRepo.findMostBorrowedBooks(startDate, endDate);
        
        return results.stream()
                .map(row -> new BookStatisticsDTO(
                        (Integer) row[0],     // bookId
                        (String) row[1],      // name
                        (String) row[2],      // author
                        (Long) row[3]         // borrowCount
                ))
                .collect(Collectors.toList());
    }
    
    // Thống kê độc giả mượn nhiều
    public List<ReaderStatisticsDTO> getMostActiveReaders(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = borrowCardRepo.findMostActiveReaders(startDate, endDate);
        
        return results.stream()
                .map(row -> new ReaderStatisticsDTO(
                        (Integer) row[0],     // readerId
                        (String) row[1],      // name
                        (String) row[2],      // email
                        (Long) row[3]         // borrowCount
                ))
                .collect(Collectors.toList());
    }
    
    // Kiểm tra có dữ liệu không
    public boolean hasDataInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return borrowCardRepo.countByBorrowDateBetween(startDate, endDate) > 0;
    }
    
    // Validate thời gian
    public boolean isValidDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        if (startDate.isAfter(endDate)) {
            return false;
        }
        if (endDate.isAfter(LocalDateTime.now())) {
            return false;
        }
        return true;
    }
}