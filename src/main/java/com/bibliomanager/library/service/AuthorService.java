package com.bibliomanager.library.service;

import com.bibliomanager.library.model.Author;
import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // Récupérer tous les auteurs
    public List<Author> findAllAuthors() {
        return (List<Author>) authorRepository.findAll();
    }

    // Compter le nombre d'auteurs
    public long countAuthors() {
        return authorRepository.count();
    }

    // Créer un auteur
    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    // Trouver un auteur par son ID
    public Author getAuthorById(Long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Auteur introuvable avec l'id " + authorId));
    }

    // Mettre à jour un auteur
    public Author updateAuthor(Long authorId, Author updatedAuthor) {
        Author existingAuthor = getAuthorById(authorId);

        existingAuthor.setAuthorFirstName(updatedAuthor.getAuthorFirstName());
        existingAuthor.setAuthorLastName(updatedAuthor.getAuthorLastName());

        return authorRepository.save(existingAuthor);
    }

    // Supprimer un auteur
    public void deleteAuthor(Long authorId) {
        Author existingAuthor = getAuthorById(authorId);
        authorRepository.delete(existingAuthor);
    }

    // Trouver un auteur par prénom + nom
    public Author getAuthorByName(String firstName, String lastName) {
        return authorRepository.findByFullName(firstName, lastName)
                .orElseThrow(() -> new EntityNotFoundException("Auteur non trouvé avec le nom : " + firstName + " " + lastName));
    }

    // Trouver tous les livres écrits par un auteur
    public List<Book> getBooksByAuthor(Long authorId) {
        getAuthorById(authorId); // Vérification que l'auteur existe
        return authorRepository.findBooksByAuthor(authorId);
    }

    // Trouver les auteurs par nom de famille
    public List<Author> getAuthorsByLastName(String lastName) {
        List<Author> authors = authorRepository.findByAuthorLastNameIgnoreCase(lastName);

        if (authors.isEmpty()) {
            throw new EntityNotFoundException("Aucun auteur trouvé avec le nom de famille : " + lastName);
        }

        return authors;
    }
}
