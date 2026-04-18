package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.BorrowCard;
import com.example.demo.model.BorrowCardDetail;
import com.example.demo.model.BorrowCardDetailId;
import com.example.demo.repository.BorrowCardRepo;
import com.example.demo.repository.ReturnRepo;

@Service
public class ReturnService {
    
    @Autowired
    private ReturnRepo returnRepo;
    
    @Autowired
    private BorrowCardRepo borrowCardRepo;
    
    /**
     * Xử lý trả sách
     * @param borrowCardId - ID phiếu mượn
     * @param bookId - ID sách
     * @param returnDate - Ngày trả (format: yyyy-MM-dd HH:mm:ss hoặc yyyy-MM-dd)
     * @param fine - Tiền phạt (đồng)
     * @return true nếu thành công, false nếu thất bại
     */
    @Transactional
    public boolean processReturn(@NonNull Integer borrowCardId, Integer bookId, String returnDate, Long fine) {
        try {
            // Tạo composite key
            BorrowCardDetailId detailId = new BorrowCardDetailId();
            detailId.setBorrowCardId(borrowCardId);
            detailId.setBookId(bookId);
            
            // Tìm chi tiết sách theo composite key
            BorrowCardDetail detail = returnRepo.findById(detailId).orElse(null);
            if (detail == null) {
                return false;
            }
            
            // Cập nhật ngày trả và trạng thái
            LocalDateTime returnDateTime;
            
            // Thử parse với format đầy đủ trước
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                returnDateTime = LocalDateTime.parse(returnDate, formatter);
            } catch (Exception e) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                returnDateTime = LocalDateTime.parse(returnDate, formatter);
            }
            
            detail.setReturnDate(returnDateTime);
            detail.setStatus("returned");
            returnRepo.save(detail);
            
            // Cộng tiền phạt vào phiếu mượn
            if (fine != null && fine > 0) {
                BorrowCard borrowCard = borrowCardRepo.findById(borrowCardId).orElse(null);
                if (borrowCard != null) {
                    BigDecimal currentFine = borrowCard.getTotalAmount() != null ? borrowCard.getTotalAmount() : BigDecimal.ZERO;
                    borrowCard.setTotalAmount(currentFine.add(BigDecimal.valueOf(fine)));
                    borrowCardRepo.save(borrowCard);
                }
            }
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tính tiền phạt
     * @param borrowCardId - ID phiếu mượn
     * @param bookId - ID sách
     * @return tiền phạt (đồng)
     */
    public long calculateFine(Integer borrowCardId, Integer bookId) {
        try {
            BorrowCardDetailId detailId = new BorrowCardDetailId();
            detailId.setBorrowCardId(borrowCardId);
            detailId.setBookId(bookId);
            
            BorrowCardDetail detail = returnRepo.findById(detailId).orElse(null);
            if (detail == null) {
                return 0;
            }
            
            LocalDateTime expireDate = detail.getExpire();
            LocalDateTime now = LocalDateTime.now();
            
            if (now.isAfter(expireDate)) {
                long daysOverdue = ChronoUnit.DAYS.between(expireDate, now);
                return daysOverdue * 2000;
            }
            
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
