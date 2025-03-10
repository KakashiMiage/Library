package com.bibliomanager.library.service;

import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.model.Genre;
import com.bibliomanager.library.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    private final GenreRepository genreRepository;
    private final AuthService authService;

    public GenreService(GenreRepository genreRepository, AuthService authService) {
        this.genreRepository = genreRepository;
        this.authService = authService;
    }

    public Iterable<Genre> getAllGenres() {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.genreRepository.findAll();
    }

    public Genre addGenre(Genre genre) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.genreRepository.save(genre);
    }

    public Optional<Genre> getGenreById(Long genreId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.genreRepository.findById(genreId);
    }

    public List<Genre> getGenreByName(String genreName) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.genreRepository.findGenreByName(genreName);
    }

    public Genre updateGenre(Long genreId, Genre updatedGenre) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        if (genreRepository.existsById(genreId)) {
            updatedGenre.setGenreId(genreId);
            return genreRepository.save(updatedGenre);
        }
        return null;
    }

    public void deleteGenre(Long genreId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        this.genreRepository.deleteById(genreId);
    }

    public long countGenres() {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.genreRepository.countGenres();
    }

    public List<Book> getBooksByGenre(Long genreId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.genreRepository.getBooksByGenre(String.valueOf(genreId));
    }
}
