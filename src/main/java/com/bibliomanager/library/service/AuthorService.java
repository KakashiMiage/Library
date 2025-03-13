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

    public List<Author> findAllAuthors() {
        return (List<Author>) authorRepository.findAll();
    }

    public long countAuthors() {
        return authorRepository.count();
    }

    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Author getAuthorById(Long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Auteur introuvable avec l'id " + authorId));
    }

    public Author updateAuthor(Long authorId, Author updatedAuthor) {
        Author existingAuthor = getAuthorById(authorId);

        existingAuthor.setAuthorFirstName(updatedAuthor.getAuthorFirstName());
        existingAuthor.setAuthorLastName(updatedAuthor.getAuthorLastName());

        return authorRepository.save(existingAuthor);
    }

    public void deleteAuthor(Long authorId) {
        Author existingAuthor = getAuthorById(authorId);
        authorRepository.delete(existingAuthor);
    }

    public Author getAuthorByName(String firstName, String lastName) {
        return authorRepository.findByFullName(firstName, lastName)
                .orElseThrow(() -> new EntityNotFoundException("Auteur non trouvé avec le nom : " + firstName + " " + lastName));
    }

    public List<Book> getBooksByAuthor(Long authorId) {
        getAuthorById(authorId); // Vérification que l'auteur existe
        return authorRepository.findBooksByAuthor(authorId);
    }

    public List<Author> getAuthorsByLastName(String lastName) {
        List<Author> authors = authorRepository.findByAuthorLastNameIgnoreCase(lastName);

        if (authors.isEmpty()) {
            throw new EntityNotFoundException("Aucun auteur trouvé avec le nom de famille : " + lastName);
        }

        return authors;
    }
}
