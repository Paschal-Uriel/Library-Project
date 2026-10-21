package com.Paschal.LibraryProject.controller;

import com.Paschal.LibraryProject.entity.Book;
import com.Paschal.LibraryProject.entity.BorrowRecord;
import com.Paschal.LibraryProject.entity.LibraryUser;
import com.Paschal.LibraryProject.entity.*;
import com.Paschal.LibraryProject.service.LibraryService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/library")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // Add a book
    @PostMapping("/books")
    public Book addBook(@RequestBody AddBookRequest request) {
        return libraryService.addBook(
                request.title(),
                request.author(),
                request.copies());
    }

    // Register a user
    @PostMapping("/users")
    public LibraryUser registerUser(
            @RequestBody RegisterUserRequest request) {
        return libraryService.registerUser(
                request.name(),
                request.email());
    }

    // Borrow a book
    @PostMapping("/borrow")
    public BorrowRecord borrowBook(
            @RequestBody BorrowRequest request) {
        return libraryService.borrowBook(
                request.userId(),
                request.bookId(),
                request.quantity());
    }

    // Return a book
    @PostMapping("/return")
    public String returnBook(
            @RequestBody ReturnRequest request) {
        return libraryService.returnBook(
                request.userId(),
                request.title(),
                request.quantity());
    }

    // Find books by author
    @GetMapping("/books/author")
    public List<Book> booksByAuthor(
            @RequestParam String author) {
        return libraryService.findBooksByAuthor(author);
    }

    // View all books
    @GetMapping("/books")
    public List<Book> allBooks() {
        return libraryService.getAllBooks();
    }

    public record AddBookRequest(
            String title, String author, int copies) {}

    public record RegisterUserRequest(
            String name, String email) {}

    public record BorrowRequest(
            Long userId, Long bookId, int quantity) {}

    public record ReturnRequest(
            Long userId, String title, int quantity) {}
}