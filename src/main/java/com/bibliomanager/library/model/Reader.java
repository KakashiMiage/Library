package com.bibliomanager.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "reader")
@NoArgsConstructor
@AllArgsConstructor
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long readerId;

    @Column(nullable = false, length = 100)
    private String readerName;

    @Column(nullable = false, unique = true, length = 50)
    private String readerUsername;

    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    // Getters
    public Long getReaderId() {
        return readerId;
    }

    public String getReaderName() {
        return readerName;
    }

    public String getReaderUsername() {
        return readerUsername;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    // Setters
    public void setReaderId(Long readerId) {
        this.readerId = readerId;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public void setReaderUsername(String readerUsername) {
        this.readerUsername = readerUsername;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
