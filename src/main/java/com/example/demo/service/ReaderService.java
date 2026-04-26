package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if (reader.isEmpty())
            return null;
        return reader.get();
    }

    public int checkBorrowingBook(int readerId) {
        return readerRepo.getQtyBorrowing(readerId);
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
