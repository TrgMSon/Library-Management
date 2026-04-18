package com.example.demo.repository;

import com.example.demo.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    
    // Thống kê sách mượn nhiều theo khoảng thời gian
    @Query("SELECT br.book.bookId, br.book.name, br.book.author, COUNT(br.id) as borrowCount " +
           "FROM BorrowRecord br " +
           "WHERE br.borrowDate BETWEEN :startDate AND :endDate " +
           "GROUP BY br.book.bookId, br.book.name, br.book.author " +
           "ORDER BY borrowCount DESC")
    List<Object[]> findMostBorrowedBooks(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    // Thống kê độc giả mượn nhiều theo khoảng thời gian
    @Query("SELECT br.user.userId, br.user.name, br.user.email, COUNT(br.id) as borrowCount " +
           "FROM BorrowRecord br " +
           "WHERE br.borrowDate BETWEEN :startDate AND :endDate " +
           "AND br.user.role = 'READER' " +
           "GROUP BY br.user.userId, br.user.name, br.user.email " +
           "ORDER BY borrowCount DESC")
    List<Object[]> findMostActiveReaders(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    // Kiểm tra có dữ liệu trong khoảng thời gian không
    @Query("SELECT COUNT(br) FROM BorrowRecord br " +
           "WHERE br.borrowDate BETWEEN :startDate AND :endDate")
    long countByBorrowDateBetween(@Param("startDate") LocalDateTime startDate, 
                                  @Param("endDate") LocalDateTime endDate);
}