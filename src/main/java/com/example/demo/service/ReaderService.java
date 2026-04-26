package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CreateCardDTO;
import com.example.demo.dto.CreateDetailCardDTO;
import com.example.demo.model.BorrowCard;
import com.example.demo.model.Reader;
import com.example.demo.repository.BorrowCardRepo;
import com.example.demo.repository.ReaderRepo;

@Service
public class ReaderService {
    @Autowired
    private ReaderRepo readerRepo;

    @Autowired
    private BorrowCardRepo borrowCardRepo;

    public Reader getReaderInfor(int readerId) {
        Optional<Reader> reader = readerRepo.findById(readerId);
        if (reader.isEmpty()) return null;
        return reader.get();   
    }

    public int checkBorrowingBook(int readerId) {
        return readerRepo.getQtyBorrowing(readerId);
    }

    public String createCard(CreateCardDTO card) {
        readerRepo.createCard(card.getUserId(), card.getReaderId(), card.getTotalAmount(), card.getNote(), LocalDateTime.now());
        return readerRepo.getCardCreatedId(card.getReaderId());
    }

    public ArrayList<String> addDetailCard(ArrayList<CreateDetailCardDTO> cardDetails) {
        ArrayList<String> bookIds = new ArrayList<>();

        for (CreateDetailCardDTO cardDetail : cardDetails) {
            String expire = cardDetail.getExpire() + "T22:00:00";
            LocalDateTime expireDate = LocalDateTime.parse(expire);
            if (readerRepo.decreaseQtyBook(1, cardDetail.getBookId()) > 0) {
                readerRepo.addDetailCard(cardDetail.getBorrowCardId(), cardDetail.getBookId(), expireDate, "borrowing");
            }
            else bookIds.add(cardDetail.getBookId() + "");
        }
        return bookIds;
    }

    public ResponseEntity<String> deleteReader(int id) {
        if (borrowCardRepo.existsBorrowCardByReader_ReaderId(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Độc giả đang có phiếu mượn, không thể xóa");
        }

        Reader reader = readerRepo.findById(id).orElse(null);
        if (reader == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Độc giả không tồn tại");
        }

        readerRepo.delete(reader);
        return ResponseEntity.ok().body("Xoá độc giả thành công!");
    }
}
