package com.bibliomanager.library.service;

import com.bibliomanager.library.model.Author;
import com.bibliomanager.library.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private final AuthorRepository authorRepository;

    @Autowired
    private AuthService authService;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> findAllAuthors() {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return (List<Author>) authorRepository.findAll();
    }

    public long countAuthors() {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return authorRepository.count();
    }

    public Author createAuthor(Author author) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return authorRepository.save(author);
    }

    public Author getAuthorById(Long authorId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Auteur introuvable !"));
    }

    public Author updateAuthor(Long authorId, Author updatedAuthor) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        Author existingAuthor = getAuthorById(authorId);
        existingAuthor.setAuthorFirstName(updatedAuthor.getAuthorFirstName());
        existingAuthor.setAuthorLastName(updatedAuthor.getAuthorLastName());
        return authorRepository.save(existingAuthor);
    }

    public void deleteAuthor(Long authorId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        authorRepository.deleteById(authorId);
    }

    public Author getAuthorByName(String firstName, String lastName) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return authorRepository.findByFullName(firstName, lastName)
                .orElseThrow(() -> new RuntimeException("Auteur non trouvé !"));
    }
}