package com.bibliomanager.library.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "reader")
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long readerId;

    @Column(nullable = false)
    private String readerName;

    @Column(nullable = false, unique = true)
    private String readerUsername;

    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    public Reader() {
    }

    public Reader(String readerName, String readerUsername) {
        this.readerName = readerName;
        this.readerUsername = readerUsername;
    }

    public Long getReaderId() {
        return readerId;
    }

    public void setReaderId(Long readerId) {
        this.readerId = readerId;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getReaderUsername() {
        return readerUsername;
    }

    public void setReaderUsername(String readerUsername) {
        this.readerUsername = readerUsername;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}

