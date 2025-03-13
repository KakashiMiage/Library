package com.bibliomanager.library.controller;

import com.bibliomanager.library.model.Genre;
import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    @Autowired
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable("id") Long genreId) {
        return ResponseEntity.ok(genreService.getGenreById(genreId));
    }

    @PostMapping
    public ResponseEntity<Genre> createGenre(@RequestBody Genre genre) {
        Genre createdGenre = genreService.createGenre(genre);
        return new ResponseEntity<>(createdGenre, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Genre> updateGenre(@PathVariable("id") Long genreId, @RequestBody Genre genre) {
        Genre updatedGenre = genreService.updateGenre(genreId, genre);
        return ResponseEntity.ok(updatedGenre);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable("id") Long genreId) {
        genreService.deleteGenre(genreId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countGenres() {
        return ResponseEntity.ok(genreService.countGenres());
    }

    @GetMapping("/search")
    public ResponseEntity<Genre> findGenreByName(@RequestParam String name) {
        Genre genre = genreService.findGenreByName(name);
        return ResponseEntity.ok(genre);
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<Book>> getBooksByGenre(@PathVariable("id") Long genreId) {
        List<Book> books = genreService.getBooksByGenre(genreId);
        return ResponseEntity.ok(books);
    }
}
