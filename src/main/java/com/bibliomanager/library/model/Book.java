package com.bibliomanager.library.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long isbn;

    @Column(nullable = false)
    private String bookTitle;

    @Temporal(TemporalType.DATE)
    private Date bookPublicationDate;

    @ManyToOne
    @JoinColumn(name = "editor_id", nullable = false)
    private Editor editor;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @Column(columnDefinition = "TEXT")
    private String bookDescription;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> bookReviews;

    @Column(nullable = false)
    private int numberOfPages;

    // Constructeurs
    public Book() {
    }

    public Book(String bookTitle, Date bookPublicationDate, Editor editor, Author author, Type type, Genre genre, String bookDescription, int numberOfPages) {
        this.bookTitle = bookTitle;
        this.bookPublicationDate = bookPublicationDate;
        this.editor = editor;
        this.author = author;
        this.type = type;
        this.genre = genre;
        this.bookDescription = bookDescription;
        this.numberOfPages = numberOfPages;
    }

    // Getters et Setters
    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Date getBookPublicationDate() {
        return bookPublicationDate;
    }

    public void setBookPublicationDate(Date bookPublicationDate) {
        this.bookPublicationDate = bookPublicationDate;
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public List<Review> getBookReviews() {
        return bookReviews;
    }

    public void setBookReviews(List<Review> bookReviews) {
        this.bookReviews = bookReviews;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }
}
