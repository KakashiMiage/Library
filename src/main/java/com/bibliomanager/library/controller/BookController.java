package com.bibliomanager.library.controller;

import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") Long bookId) {
        return ResponseEntity.ok(bookService.getBookById(bookId));
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") Long bookId, @RequestBody Book book) {
        Book updatedBook = bookService.update(bookId, book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countBooks() {
        return ResponseEntity.ok(bookService.countBooks());
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<Book>> searchBooksByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.findBooksByTitle(title));
    }

    @GetMapping("/search/author/{authorId}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId));
    }

    @GetMapping("/search/genre/{genreId}")
    public ResponseEntity<List<Book>> getBooksByGenre(@PathVariable Long genreId) {
        return ResponseEntity.ok(bookService.getBooksByGenre(genreId));
    }

    @GetMapping("/search/editor/{editorId}")
    public ResponseEntity<List<Book>> getBooksByEditor(@PathVariable Long editorId) {
        return ResponseEntity.ok(bookService.getBooksByEditor(editorId));
    }

    @GetMapping("/search/type/{typeId}")
    public ResponseEntity<List<Book>> getBooksByType(@PathVariable Long typeId) {
        return ResponseEntity.ok(bookService.getBooksByType(typeId));
    }

    @GetMapping("/search/keyword")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String keyword) {
        return ResponseEntity.ok(bookService.searchBooks(keyword));
    }

    @GetMapping("/reviews/not-empty")
    public ResponseEntity<List<Book>> getBooksWithReviews() {
        return ResponseEntity.ok(bookService.getBooksWithReviews());
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<Book>> getTopRatedBooks(@RequestParam int minRating) {
        return ResponseEntity.ok(bookService.getTopRatedBooks(minRating));
    }
}
