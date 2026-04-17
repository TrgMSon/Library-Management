package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.demo.model.BorrowCard;
import com.example.demo.repository.BorrowCardRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CreateCardDTO;
import com.example.demo.dto.CreateDetailCardDTO;
import com.example.demo.model.Reader;
import com.example.demo.repository.ReaderRepo;

@Service
@RequiredArgsConstructor
public class ReaderService {
    private final ReaderRepo readerRepo;
    private final BorrowCardRepo borrowCardRepo;

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

    @Transactional
    public void deleteReader(int id) {
        Reader reader = readerRepo.findById(id).orElse(null);
        if (reader != null) {
            List<BorrowCard> borrowCardList = borrowCardRepo.findByReader(reader);
            if (borrowCardList != null) {
                for (BorrowCard borrowCard : borrowCardList) {
                    borrowCard.setReader(null);
                }

                borrowCardRepo.saveAll(borrowCardList);
                readerRepo.delete(reader);
            }
        }
    }
}
