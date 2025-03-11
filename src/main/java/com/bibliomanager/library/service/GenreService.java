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

    // Récupérer tous les genres
    public List<Genre> getAllGenres() {
        return (List<Genre>) genreRepository.findAll();
    }

    // Compter les genres
    public long countGenres() {
        return genreRepository.count();
    }

    // Créer un genre
    public Genre createGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    // Trouver un genre par ID
    public Genre getGenreById(Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre introuvable avec l'id " + genreId));
    }

    // Mettre à jour un genre
    public Genre updateGenre(Long genreId, Genre updatedGenre) {
        Genre existingGenre = getGenreById(genreId);

        existingGenre.setGenreName(updatedGenre.getGenreName());

        return genreRepository.save(existingGenre);
    }

    // Supprimer un genre
    public void deleteGenre(Long genreId) {
        Genre existingGenre = getGenreById(genreId);
        genreRepository.delete(existingGenre);
    }

    // Trouver un genre par son nom
    public Genre findGenreByName(String genreName) {
        return genreRepository.findByGenreNameIgnoreCase(genreName)
                .orElseThrow(() -> new EntityNotFoundException("Genre non trouvé avec le nom : " + genreName));
    }

    // Récupérer tous les livres associés à un genre
    public List<Book> getBooksByGenre(Long genreId) {
        getGenreById(genreId); // Vérifie que le genre existe
        return genreRepository.findBooksByGenre(genreId);
    }
}
