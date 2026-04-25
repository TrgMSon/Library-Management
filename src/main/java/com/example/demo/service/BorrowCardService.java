package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BookInCardDTO;
import com.example.demo.dto.BorrowCardDTO;
import com.example.demo.dto.BorrowCardDetailDTO;
import com.example.demo.dto.CreateCardDTO;
import com.example.demo.dto.CreateDetailCardDTO;
import com.example.demo.model.BorrowCard;
import com.example.demo.model.BorrowCardDetail;
import com.example.demo.repository.BookRepo;
import com.example.demo.repository.BorrowCardRepo;

@Service
public class BorrowCardService {
    @Autowired
    private BorrowCardRepo borrowCardRepo;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private BookService bookService;

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
            detailCard.setBorrowCardDTO(new BorrowCardDTO());
            detailCard.getBorrowCardDTO().setBorrowCardId(cardId);
            detailCard.getBorrowCardDTO().setUserId(tmp.getUser().getUserId());
            detailCard.getBorrowCardDTO().setUserName(tmp.getUser().getName());
            detailCard.getBorrowCardDTO().setReaderId(tmp.getReader().getReaderId());
            detailCard.getBorrowCardDTO().setReaderName(tmp.getReader().getName());
            detailCard.setTotalAmount(tmp.getTotalAmount());
            detailCard.getBorrowCardDTO().setCreatedAt(tmp.getCreatedAt());
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

    public void deleteCard(int cardId) {
        borrowCardRepo.deleteById(cardId);
    }

    public String createCard(CreateCardDTO card) {
        borrowCardRepo.createCard(card.getUserId(), card.getReaderId(), card.getTotalAmount(), card.getNote(),
                LocalDateTime.now());
        return borrowCardRepo.getCardCreatedId(card.getReaderId());
    }

    public ArrayList<String> addDetailCard(ArrayList<CreateDetailCardDTO> cardDetails) {
        ArrayList<String> bookIds = new ArrayList<>();

        int tmp = 0;
        for (CreateDetailCardDTO cardDetail : cardDetails) {
            try {
                tmp = Integer.parseInt(cardDetail.getBookId());

                if (bookRepo.findQty(tmp) == null) {

                    bookIds.add("invalidBook");
                    bookIds.add("invalidBook-" + cardDetail.getBookId());
                    continue;
                }
                if (!bookService.isInStock(tmp, 1)) {
                    bookIds.add(tmp + "");
                }
            } catch (Exception e) {
                bookIds.add("invalidBook");
                bookIds.add("invalidBook-" + cardDetail.getBookId());
            }

        }

        if (bookIds.size() == 0) {
            for (CreateDetailCardDTO cardDetail : cardDetails) {
                String expire = cardDetail.getExpire() + "T22:00:00";
                LocalDateTime expireDate = LocalDateTime.parse(expire);
                borrowCardRepo.addDetailCard(cardDetail.getBorrowCardId(), Integer.parseInt(cardDetail.getBookId()), expireDate, "borrowing");
                bookRepo.decreaseQtyBook(1, Integer.parseInt(cardDetail.getBookId()));
            }
        }

        return bookIds;
    }
}
