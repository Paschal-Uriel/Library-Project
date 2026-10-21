package com.Paschal.LibraryProject.service;

import com.Paschal.LibraryProject.entity.Book;
import com.Paschal.LibraryProject.entity.BorrowRecord;
import com.Paschal.LibraryProject.entity.LibraryUser;
import com.Paschal.LibraryProject.repository.BookRepository;
import com.Paschal.LibraryProject.repository.BorrowRecordRepository;
import com.Paschal.LibraryProject.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LibraryService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRepository;

    public LibraryService(BookRepository bookRepository, UserRepository userRepository, BorrowRecordRepository borrowRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowRepository = borrowRepository;
    }

    public Book addBook(String title, String author, int copies){

        if (copies < 1) {
            throw new IllegalArgumentException("Copies must be at least 1.");
        }

        Book book = new Book(title, author, copies);
        return bookRepository.save(book);
    }

    public LibraryUser registerUser(String name, String email) {

        LibraryUser user = new LibraryUser(name, email);
        return userRepository.save(user);
    }

    @Transactional
    public BorrowRecord borrowBook(Long userId, Long bookId, int quantiy) {

        if (quantiy < 1) {
            throw new IllegalArgumentException("Borrow quantity must be at least 1.");
        }

        LibraryUser user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found."));

        boolean hasUnReturnedBooks = borrowRepository.existsByUser_IdAndOustandingCopiesGreaterThan(userId, 0);

        if (hasUnReturnedBooks) {
            throw new IllegalStateException("You must return all borrowed books " + "before borrowing another book.");
        }

        if (book.getAvailableCopies() - quantiy < 1) {
            throw new IllegalStateException("Borrowing denied. At least one copy " + "of this book must remain in the library.");
        }

        book.setAvailableCopies(book.getAvailableCopies() - quantiy);
        bookRepository.save(book);

        BorrowRecord record = new BorrowRecord(user, book, quantiy);
        return borrowRepository.save(record);
    }

    @Transactional
    public String returnBook(Long userId, String title, int quantity) {

        if (quantity < 1) {
            throw new IllegalArgumentException("Return quantity must be at least 1.");
        }

        userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));

        List<BorrowRecord> records = borrowRepository.findByUser_IdAndBook_TitleIgnoreCaseAndOutstandingCopiesGreaterThan(userId, title,0);

        if (records.isEmpty()) {
            throw new IllegalStateException("No outstanding borrowing record found " + "for this user and book");
        }

        BorrowRecord record = records.get(0);

        if (quantity > record.getOutstandingCopies()) {
            throw new IllegalArgumentException("You cannot return more copies " + "than you borrowed and stil owe");
        }

        Book book = record.getBook();

        book.setAvailableCopies(book.getAvailableCopies() + quantity);
        bookRepository.save(book);

        record.setOutstandingCopies(record.getOutstandingCopies() - quantity);
        borrowRepository.save(record);

        if (record.getOutstandingCopies() == 0) {
            return "Book returned successfully." + "You can now borrow another book.";
        }

        return "Book returned successfully. You still have " + record.getOutstandingCopies() + " outstanding copies.";
    }

    public List<Book> findBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainsIgnoreCase(author);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
