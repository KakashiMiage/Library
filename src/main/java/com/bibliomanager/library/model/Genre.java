package com.bibliomanager.library.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "genre")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer genreId;

    @Column(nullable = false, unique = true)
    private String genreName;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(name = "type_genre", joinColumns = {@JoinColumn(name = "genre_id")}, inverseJoinColumns = {@JoinColumn(name = "type_id")})
    public List<Type> types;

    public Genre() {
    }

    public Genre(String genreName) {
        this.genreName = genreName;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
