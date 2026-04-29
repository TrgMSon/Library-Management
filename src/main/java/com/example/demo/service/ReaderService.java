package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ReaderDTO;
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

    public ResponseEntity<String> addReader(ReaderDTO readerDTO) {
        String name = readerDTO.getName();
        String email = readerDTO.getEmail();
        String address = readerDTO.getAddress();

        Reader reader = new Reader();
        reader.setName(name);
        reader.setEmail(email);
        reader.setAddress(address);

        readerRepo.save(reader);
        return ResponseEntity.ok().body("Thêm độc giả thành công!");
    }

    public ResponseEntity<String> updateReader(Reader updatedReader) {
        if (!readerRepo.existsById(updatedReader.getReaderId())) {
            return ResponseEntity.notFound().build();
        }

        readerRepo.save(updatedReader);
        return ResponseEntity.ok("Cập nhật thành công!");
    }
}
