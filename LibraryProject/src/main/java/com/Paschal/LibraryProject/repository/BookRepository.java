package com.Paschal.LibraryProject.repository;

import com.Paschal.LibraryProject.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByAuthorContainsIgnoreCase(String author);
}