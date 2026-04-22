package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CreateCardDTO;
import com.example.demo.dto.CreateDetailCardDTO;
import com.example.demo.model.BorrowCard;
import com.example.demo.model.Reader;
import com.example.demo.repository.BookRepo;
import com.example.demo.repository.BorrowCardRepo;
import com.example.demo.repository.ReaderRepo;

@Service
public class ReaderService {
    @Autowired
    private ReaderRepo readerRepo;

    @Autowired
    private BorrowCardRepo borrowCardRepo;

    @Autowired
    private BookRepo bookRepo;

    public Reader getReaderInfor(int readerId) {
        Optional<Reader> reader = readerRepo.findById(readerId);
        if (reader.isEmpty())
            return null;
        return reader.get();
    }

    public int checkBorrowingBook(int readerId) {
        return readerRepo.getQtyBorrowing(readerId);
    }

    public String createCard(CreateCardDTO card) {
        readerRepo.createCard(card.getUserId(), card.getReaderId(), card.getTotalAmount(), card.getNote(),
                LocalDateTime.now());
        return readerRepo.getCardCreatedId(card.getReaderId());
    }

    public boolean isInStock(int bookId, int cartQty) {
        return cartQty <= bookRepo.findQty(bookId);
    }

    public ArrayList<String> addDetailCard(ArrayList<CreateDetailCardDTO> cardDetails) {
        ArrayList<String> bookIds = new ArrayList<>();

        for (CreateDetailCardDTO cardDetail : cardDetails) {
            if (bookRepo.findQty(cardDetail.getBookId()) == null) {
                bookIds.add("invalidBook");
                bookIds.add("invalidBook-" + cardDetail.getBookId());
                continue;
            }
            if (!isInStock(cardDetail.getBookId(), 1)) {
                bookIds.add(cardDetail.getBookId() + "");
            }
        }

        if (bookIds.size() == 0) {
            for (CreateDetailCardDTO cardDetail : cardDetails) {
                String expire = cardDetail.getExpire() + "T22:00:00";
                LocalDateTime expireDate = LocalDateTime.parse(expire);
                readerRepo.addDetailCard(cardDetail.getBorrowCardId(), cardDetail.getBookId(), expireDate, "borrowing");
                readerRepo.decreaseQtyBook(1, cardDetail.getBookId());
            }
        }

        return bookIds;
    }

    public boolean deleteReader(int id) {
        Reader reader = readerRepo.findById(id).orElse(null);
        if (reader != null) {
            List<BorrowCard> borrowCardList = borrowCardRepo.findByReader(reader);
            if (borrowCardList != null)
                return false;
            else {
                readerRepo.delete(reader);
                return true;
            }
        }
        return false;
    }
}
