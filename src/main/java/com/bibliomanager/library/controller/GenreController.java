package com.bibliomanager.library.controller;

import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.model.Genre;
import com.bibliomanager.library.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class GenreController {

    @Autowired
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/genres")
    public ResponseEntity<Iterable<Genre>> getAllGenres() {
        return ResponseEntity.ok(this.genreService.getAllGenres());
    }

    @PostMapping("/genres")
    public ResponseEntity<Genre> addGenre(@RequestBody Genre genre) {
        return ResponseEntity.ok(this.genreService.addGenre(genre));
    }

    @GetMapping("/genres/{id}")
    public ResponseEntity<Optional<Genre>> getGenreById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.genreService.getGenreById(id));
    }

    @GetMapping("/genres/name/{genreName}")
    public ResponseEntity<List<Genre>> getGenreByName(@PathVariable String genreName) {
        return ResponseEntity.ok(this.genreService.getGenreByName(genreName));
    }

    @PutMapping("/genres/{id}")
    public ResponseEntity<Void> updateGenre(@PathVariable Integer id, @RequestParam String genreName) {
        this.genreService.updateGenre(id, genreName);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/genres/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Integer id) {
        this.genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/genres/count")
    public ResponseEntity<Long> countGenres() {
        return ResponseEntity.ok(this.genreService.countGenres());
    }

    @GetMapping("/genres/{id}/books")
    public ResponseEntity<List<Book>> getBooksByGenre(@PathVariable Integer id) {
        return ResponseEntity.ok(this.genreService.getBooksByGenre(id));
    }
}
