package com.example.demo.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BookInCardDTO;
import com.example.demo.dto.BorrowCardDTO;
import com.example.demo.dto.BorrowCardDetailDTO;
import com.example.demo.model.BorrowCard;
import com.example.demo.model.BorrowCardDetail;
import com.example.demo.repository.BorrowCardRepo;

@Service
public class BorrowCardService {
    @Autowired
    private BorrowCardRepo borrowCardRepo;

    public ArrayList<BorrowCardDTO> getAllBorrowCard() {
        ArrayList<BorrowCard> borrowCards = borrowCardRepo.findAllBorrowCards();
        ArrayList<BorrowCardDTO> borrowCardDTOs = new ArrayList<>();

        for (BorrowCard borrowCard : borrowCards) {
            borrowCardDTOs.add(new BorrowCardDTO(borrowCard.getBorrowCardId(), borrowCard.getReader().getReaderId(),
                    borrowCard.getReader().getName(), borrowCard.getUser().getUserId(), borrowCard.getUser().getName(),
                    borrowCard.getCreatedAt()));
        }
        return borrowCardDTOs;
    }

    public BorrowCardDetailDTO getDetailCard(int cardId) {
        BorrowCardDetailDTO detailCard = new BorrowCardDetailDTO();
        Optional<BorrowCard> borrowCard = borrowCardRepo.findById(cardId);
        if (!borrowCard.isEmpty()) {
            BorrowCard tmp = borrowCard.get();
            detailCard.setBorrowCardId(cardId);
            detailCard.setUserId(tmp.getUser().getUserId());
            detailCard.setUserName(tmp.getUser().getName());
            detailCard.setReaderId(tmp.getReader().getReaderId());
            detailCard.setReaderName(tmp.getReader().getName());
            detailCard.setTotalAmount(tmp.getTotalAmount());
            detailCard.setCreatedAt(tmp.getCreatedAt());
        }

        ArrayList<BorrowCardDetail> borrowCardDetails = borrowCardRepo.findBorrowCardDetail(cardId);
        ArrayList<BookInCardDTO> books = new ArrayList<>();
        for (BorrowCardDetail borrowCardDetail : borrowCardDetails) {
            books.add(new BookInCardDTO(borrowCardDetail.getBook().getBookId(), borrowCardDetail.getBook().getName(),
                    borrowCardDetail.getStatus(), borrowCardDetail.getExpire(), borrowCardDetail.getReturnDate()));
        }
        detailCard.setBooks(books);

        return detailCard;
    }

    public ArrayList<BorrowCardDTO> searchCard(String target, String option) {
        ArrayList<BorrowCard> borrowCards = new ArrayList<>();
        if (option.equals("all"))
            borrowCards = borrowCardRepo.findCardById(Integer.parseInt(target));
        else if (option.equals("borrowing"))
            borrowCards = borrowCardRepo.findBorrowingCard(Integer.parseInt(target));
        else
            borrowCards = borrowCardRepo.findReturnedCard(Integer.parseInt(target));

        ArrayList<BorrowCardDTO> borrowCardDTOs = new ArrayList<>();
        for (BorrowCard borrowCard : borrowCards) {
            borrowCardDTOs.add(new BorrowCardDTO(borrowCard.getBorrowCardId(), borrowCard.getReader().getReaderId(),
                    borrowCard.getReader().getName(), borrowCard.getUser().getUserId(), borrowCard.getUser().getName(),
                    borrowCard.getCreatedAt()));
        }
        return borrowCardDTOs;
    }
}
