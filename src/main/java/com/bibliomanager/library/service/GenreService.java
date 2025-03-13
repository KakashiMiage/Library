package com.bibliomanager.library.service;

import com.bibliomanager.library.model.Genre;
import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    @Autowired
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getAllGenres() {
        return (List<Genre>) genreRepository.findAll();
    }

    public long countGenres() {
        return genreRepository.count();
    }

    public Genre createGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    public Genre getGenreById(Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id " + genreId));
    }

    public Genre updateGenre(Long genreId, Genre updatedGenre) {
        Genre existingGenre = getGenreById(genreId);

        existingGenre.setGenreName(updatedGenre.getGenreName());

        return genreRepository.save(existingGenre);
    }

    public void deleteGenre(Long genreId) {
        Genre existingGenre = getGenreById(genreId);
        genreRepository.delete(existingGenre);
    }

    public Genre findGenreByName(String genreName) {
        return genreRepository.findByGenreNameIgnoreCase(genreName)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with name : " + genreName));
    }

    public List<Book> getBooksByGenre(Long genreId) {
        getGenreById(genreId);
        return genreRepository.findBooksByGenre(genreId);
    }
}
