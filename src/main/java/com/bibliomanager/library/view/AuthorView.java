package com.bibliomanager.library.view;

import com.bibliomanager.library.model.Author;
import com.bibliomanager.library.service.AuthorService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.List;

@Named("authorView")
@ViewScoped
public class AuthorView implements Serializable {

    private List<Author> authors;
    private Author selectedAuthor;

    @Autowired
    private AuthorService authorService;

    @PostConstruct
    public void init() {
        authors = authorService.findAllAuthors();
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Author getSelectedAuthor() {
        return selectedAuthor;
    }

    public void setSelectedAuthor(Author selectedAuthor) {
        this.selectedAuthor = selectedAuthor;
    }

    public void deleteAuthor(Long authorId) {
        authorService.deleteAuthor(authorId);
        authors = authorService.findAllAuthors(); // Refresh list after deletion
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Author deleted", "ID: " + authorId);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}