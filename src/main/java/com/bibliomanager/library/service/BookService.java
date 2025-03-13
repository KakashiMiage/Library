package com.bibliomanager.library.service;

import com.bibliomanager.library.model.*;
import com.bibliomanager.library.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.ArrayList;

@Service
public class BookService {

    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final AuthorRepository authorRepository;

    @Autowired
    private final EditorRepository editorRepository;

    @Autowired
    private final TypeRepository typeRepository;

    @Autowired
    private final GenreRepository genreRepository;

    @Autowired
    public BookService(
            BookRepository bookRepository,
            AuthorRepository authorRepository,
            EditorRepository editorRepository,
            TypeRepository typeRepository,
            GenreRepository genreRepository
    ) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.editorRepository = editorRepository;
        this.typeRepository = typeRepository;
        this.genreRepository = genreRepository;
    }

    public Book createBook(Book book) {

        Author author = authorRepository.findById(book.getAuthor().getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id " + book.getAuthor().getAuthorId()));

        Editor editor = editorRepository.findById(book.getEditor().getEditorId())
                .orElseThrow(() -> new EntityNotFoundException("Editor not found with id " + book.getEditor().getEditorId()));

        Type type = typeRepository.findById(book.getType().getTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Type not found with id " + book.getType().getTypeId()));

        List<Genre> genres = new ArrayList<>();
        for (Genre genre : book.getGenres()) {
            Genre foundGenre = genreRepository.findById(genre.getGenreId())
                    .orElseThrow(() -> new EntityNotFoundException("Genre not found with id " + genre.getGenreId()));
            genres.add(foundGenre);
        }

        if (genres.isEmpty()) {
            throw new EntityNotFoundException("At least one valid genre is required.");
        }

        book.setAuthor(author);
        book.setEditor(editor);
        book.setType(type);
        book.setGenres(genres);

        return bookRepository.save(book);
    }

    public List<Book> findAllBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + bookId));
    }

    public Book update(Long bookId, Book updatedBook) {

        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + bookId));

        Author author = authorRepository.findById(updatedBook.getAuthor().getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id " + updatedBook.getAuthor().getAuthorId()));

        Editor editor = editorRepository.findById(updatedBook.getEditor().getEditorId())
                .orElseThrow(() -> new EntityNotFoundException("Editor not found with id " + updatedBook.getEditor().getEditorId()));

        Type type = typeRepository.findById(updatedBook.getType().getTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Type not found with id " + updatedBook.getType().getTypeId()));

        List<Genre> genres = new ArrayList<>();
        for (Genre genre : updatedBook.getGenres()) {
            Genre foundGenre = genreRepository.findById(genre.getGenreId())
                    .orElseThrow(() -> new EntityNotFoundException("Genre not found with id " + genre.getGenreId()));
            genres.add(foundGenre);
        }

        if (genres.isEmpty()) {
            throw new EntityNotFoundException("At least one valid genre is required.");
        }

        existingBook.setBookTitle(updatedBook.getBookTitle());
        existingBook.setBookPublicationDate(updatedBook.getBookPublicationDate());
        existingBook.setAuthor(author);
        existingBook.setEditor(editor);
        existingBook.setType(type);
        existingBook.setGenres(genres);
        existingBook.setBookDescription(updatedBook.getBookDescription());
        existingBook.setNumberOfPages(updatedBook.getNumberOfPages());

        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long bookId) {
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + bookId));
        bookRepository.delete(existingBook);
    }

    public List<Book> findBooksByTitle(String title) {
        return bookRepository.findByBookTitleContainingIgnoreCase(title);
    }

    public long countBooks() {
        return bookRepository.count();
    }

    public List<Book> getBooksByAuthor(Long authorId) {
        return bookRepository.findByAuthorAuthorId(authorId);
    }

    public List<Book> getBooksByGenre(Long genreId) {
        return bookRepository.findByGenresGenreId(genreId);
    }

    public List<Book> getBooksByEditor(Long editorId) {
        return bookRepository.findByEditorEditorId(editorId);
    }

    public List<Book> getBooksByType(Long typeId) {
        return bookRepository.findByTypeTypeId(typeId);
    }


    private String normalize(String input) {
        return Normalizer
        .normalize(input, Normalizer.Form.NFD)
        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
        .toLowerCase();
    }

    public List<Book> searchBooks(String keyword) {
        String normalizedKeyword = normalize(keyword);
        return bookRepository.searchBooks(normalizedKeyword);
    }


    public List<Book> getBooksWithReviews() {
        return bookRepository.findByBookReviewsIsNotEmpty();
    }

    public List<Book> getTopRatedBooks(int minRating) {
        return bookRepository.findTopRatedBooks(minRating);
    }
}
