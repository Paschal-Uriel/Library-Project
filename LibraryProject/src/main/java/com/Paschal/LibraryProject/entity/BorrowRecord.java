package com.Paschal.LibraryProject.entity;

import jakarta.persistence.*;

@Entity
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private LibraryUser user;

    @ManyToOne(optional = false)
    private Book book;

    private int borrowedCopies;
    private int outstandingCopies;

    public BorrowRecord() {
    }

    public BorrowRecord(LibraryUser user, Book book, int quantity) {
        this.user = user;
        this.book = book;
        this.borrowedCopies = quantity;
        this.outstandingCopies = quantity;
    }

    public Long getId() {
        return id;
    }

    public LibraryUser getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public int getBorrowedCopies() {
        return borrowedCopies;
    }

    public int getOutstandingCopies() {
        return outstandingCopies;
    }

    public void setOutstandingCopies(int quantity) {
        this.outstandingCopies = quantity;
    }
}
