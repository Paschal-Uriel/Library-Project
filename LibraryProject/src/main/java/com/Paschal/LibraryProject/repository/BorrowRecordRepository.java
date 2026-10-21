package com.Paschal.LibraryProject.repository;

import com.Paschal.LibraryProject.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    boolean existsByUser_IdAndOustandingCopiesGreaterThan(Long userId, int quantity);

    List<BorrowRecord> findByUser_IdAndBook_TitleIgnoreCaseAndOutstandingCopiesGreaterThan(Long userId, String title, int quantity);
}
