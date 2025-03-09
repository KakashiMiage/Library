package com.bibliomanager.library.controller;

import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.createBook(book));
    }

    @GetMapping
    public ResponseEntity<List<Book>> findAllBooks() {
        return ResponseEntity.ok(bookService.findAllBooks());
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookById(bookId));
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<Book> update(@PathVariable Long bookId, @RequestBody Book updatedBook) {
        return ResponseEntity.ok(bookService.update(bookId, updatedBook));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<Book>> findBooksByTitle(@PathVariable String title) {
        return ResponseEntity.ok(bookService.findBooksByTitle(title));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countBooks() {
        return ResponseEntity.ok(bookService.countBooks());
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId));
    }

    @GetMapping("/genre/{genreId}")
    public ResponseEntity<List<Book>> getBooksByGenre(@PathVariable Long genreId) {
        return ResponseEntity.ok(bookService.getBooksByGenre(genreId));
    }

    @GetMapping("/editor/{editorId}")
    public ResponseEntity<List<Book>> getBooksByEditor(@PathVariable Long editorId) {
        return ResponseEntity.ok(bookService.getBooksByEditor(editorId));
    }

    @GetMapping("/type/{typeId}")
    public ResponseEntity<List<Book>> getBooksByType(@PathVariable Long typeId) {
        return ResponseEntity.ok(bookService.getBooksByType(typeId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String keyword) {
        return ResponseEntity.ok(bookService.searchBooks(keyword));
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<Book>> getBooksWithReviews() {
        return ResponseEntity.ok(bookService.getBooksWithReviews());
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<Book>> getTopRatedBooks(@RequestParam int minRating) {
        return ResponseEntity.ok(bookService.getTopRatedBooks(minRating));
    }
}