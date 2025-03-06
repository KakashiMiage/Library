package com.bibliomanager.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review")
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private int reviewRate;

    @Column(length = 1000)
    private String reviewDescription;

    @ManyToOne
    @JoinColumn(name = "reader_id", nullable = false)
    private Reader reader;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // Getters
    public Long getReviewId() {
        return reviewId;
    }

    public int getReviewRate() {
        return reviewRate;
    }

    public String getReviewDescription() {
        return reviewDescription;
    }

    public Reader getReader() {
        return reader;
    }

    public Book getBook() {
        return book;
    }

    // Setters
    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public void setReviewRate(int reviewRate) {
        this.reviewRate = reviewRate;
    }

    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
