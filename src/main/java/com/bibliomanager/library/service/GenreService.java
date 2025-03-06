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

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Iterable<Genre> getAllGenres() {
        return this.genreRepository.findAll();
    }

    public Genre addGenre(Genre genre) {
        return this.genreRepository.save(genre);
    }

    public Optional<Genre> getGenreById(Integer genreId) {
        return this.genreRepository.findById(genreId);
    }

    public List<Genre> getGenreByName(String genreName) {
        return this.genreRepository.findGenreByName(genreName);
    }

    public void updateGenre(Integer genreId, String genreName) {
        this.genreRepository.updateGenre(genreId, genreName);
    }

    public void deleteGenre(Integer genreId) {
        this.genreRepository.deleteById(genreId);
    }

    public long countGenres() {
        return this.genreRepository.countGenres();
    }

    public List<Book> getBooksByGenre(Integer genreId) {
        return this.genreRepository.getBooksByGenre(String.valueOf(genreId));
    }
}
