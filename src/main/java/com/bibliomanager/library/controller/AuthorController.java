package com.bibliomanager.library.controller;

import com.bibliomanager.library.model.Author;
import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    @Autowired
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok(authorService.findAllAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable("id") Long authorId) {
        return ResponseEntity.ok(authorService.getAuthorById(authorId));
    }

    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        Author createdAuthor = authorService.createAuthor(author);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable("id") Long authorId, @RequestBody Author author) {
        Author updatedAuthor = authorService.updateAuthor(authorId, author);
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable("id") Long authorId) {
        authorService.deleteAuthor(authorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countAuthors() {
        return ResponseEntity.ok(authorService.countAuthors());
    }

    @GetMapping("/search/fullname")
    public ResponseEntity<Author> getAuthorByName(@RequestParam String firstName, @RequestParam String lastName) {
        Author author = authorService.getAuthorByName(firstName, lastName);
        return ResponseEntity.ok(author);
    }

    @GetMapping("/search/lastname/{lastName}")
    public ResponseEntity<List<Author>> getAuthorsByLastName(@PathVariable String lastName) {
        List<Author> authors = authorService.getAuthorsByLastName(lastName);
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable("id") Long authorId) {
        List<Book> books = authorService.getBooksByAuthor(authorId);
        return ResponseEntity.ok(books);
    }
}
