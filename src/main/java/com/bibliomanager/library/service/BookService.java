package com.bibliomanager.library.service;

import com.bibliomanager.library.model.*;
import com.bibliomanager.library.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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
    private final AuthService authService;

    @Autowired
    public BookService(
            BookRepository bookRepository,
            AuthorRepository authorRepository,
            EditorRepository editorRepository,
            TypeRepository typeRepository,
            GenreRepository genreRepository,
            AuthService authService
    ) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.editorRepository = editorRepository;
        this.typeRepository = typeRepository;
        this.genreRepository = genreRepository;
        this.authService = authService;
    }

    public Book createBook(Book book) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        // Vérifier si l'auteur existe déjà en base
        Author author = authorRepository.findById(book.getAuthor().getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id " + book.getAuthor().getAuthorId()));

        // Vérifier si l'éditeur existe déjà en base
        Editor editor = editorRepository.findById(book.getEditor().getEditorId())
                .orElseThrow(() -> new EntityNotFoundException("Editor not found with id " + book.getEditor().getEditorId()));

        // Vérifier si le type existe déjà en base
        Type type = typeRepository.findById(book.getType().getTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Type not found with id " + book.getType().getTypeId()));

        // Vérifier si le genre existe déjà en base
        Genre genre = genreRepository.findById(book.getGenre().getGenreId())
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id " + book.getGenre().getGenreId()));

        // Associer les entités existantes au livre
        book.setAuthor(author);
        book.setEditor(editor);
        book.setType(type);
        book.setGenre(genre);

        // Sauvegarder le livre avec les entités gérées par Hibernate
        return bookRepository.save(book);
    }

    public List<Book> findAllBooks() {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return (List<Book>) bookRepository.findAll();
    }

    public Book getBookById(Long bookId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public Book update(Long bookId, Book updatedBook) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        // Récupérer le livre existant en base
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + bookId));

        // Vérifier si l'auteur existe déjà en base
        Author author = authorRepository.findById(updatedBook.getAuthor().getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id " + updatedBook.getAuthor().getAuthorId()));

        // Vérifier si l'éditeur existe déjà en base
        Editor editor = editorRepository.findById(updatedBook.getEditor().getEditorId())
                .orElseThrow(() -> new EntityNotFoundException("Editor not found with id " + updatedBook.getEditor().getEditorId()));

        // Vérifier si le type existe déjà en base
        Type type = typeRepository.findById(updatedBook.getType().getTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Type not found with id " + updatedBook.getType().getTypeId()));

        // Vérifier si le genre existe déjà en base
        Genre genre = genreRepository.findById(updatedBook.getGenre().getGenreId())
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id " + updatedBook.getGenre().getGenreId()));

        // Mettre à jour les champs de l'objet existant
        existingBook.setBookTitle(updatedBook.getBookTitle());
        existingBook.setBookPublicationDate(updatedBook.getBookPublicationDate());
        existingBook.setEditor(editor);
        existingBook.setAuthor(author);
        existingBook.setType(type);
        existingBook.setGenre(genre);
        existingBook.setBookDescription(updatedBook.getBookDescription());
        existingBook.setNumberOfPages(updatedBook.getNumberOfPages());

        // Sauvegarder et retourner l'objet mis à jour
        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long bookId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        bookRepository.deleteById(bookId);
    }

    public List<Book> findBooksByTitle(String title) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return bookRepository.findByBookTitleContainingIgnoreCase(title);
    }

    public long countBooks() {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return bookRepository.count();
    }

    public List<Book> getBooksByAuthor(Long authorId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return bookRepository.findByAuthorId(authorId);
    }

    public List<Book> getBooksByGenre(Long genreId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return bookRepository.findByGenreId(genreId);
    }

    public List<Book> getBooksByEditor(Long editorId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return bookRepository.findByEditorId(editorId);
    }

    public List<Book> getBooksByType(Long typeId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return bookRepository.findByTypeId(typeId);
    }

    public List<Book> searchBooks(String keyword) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return bookRepository.findByBookTitleContainingIgnoreCase(keyword);
    }

    public List<Book> getBooksWithReviews() {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return bookRepository.findByBookReviewsIsNotEmpty();
    }

    public List<Book> getTopRatedBooks(int minRating) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return bookRepository.findByBookReviewsRatingGreaterThanEqual(minRating);
    }
}