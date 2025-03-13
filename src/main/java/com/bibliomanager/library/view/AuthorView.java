package com.bibliomanager.library.view;

import com.bibliomanager.library.controller.AuthorController;
import com.bibliomanager.library.model.Author;
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

    // Champs individuels pour l'ajout et la mise à jour
    private String newFirstName;
    private String newLastName;
    private String selectedFirstName;
    private String selectedLastName;
    private Long selectedAuthorId; // ID de l'auteur sélectionné pour la mise à jour

    @Autowired
    private AuthorController authorController;

    @PostConstruct
    public void init() {
        refreshAuthors();
    }

    public void refreshAuthors() {
        authors = authorController.getAllAuthors().getBody();
    }

    public void deleteAuthor(Long authorId) {
        if (authorId != null) {
            authorController.deleteAuthor(authorId);
            refreshAuthors(); // Rafraîchir la liste
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Auteur supprimé avec succès"));
        }
    }

    public void addAuthor() {
        if (newFirstName != null && !newFirstName.isEmpty() && newLastName != null && !newLastName.isEmpty()) {
            Author newAuthor = new Author();
            newAuthor.setAuthorFirstName(newFirstName);
            newAuthor.setAuthorLastName(newLastName);
            authorController.createAuthor(newAuthor);
            newFirstName = "";
            newLastName = "";
            refreshAuthors();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Author added"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "First name and last name are required"));
        }
    }

    public List<Author> getAuthors() {
        return authors;
    }

    // Getters et setters pour les nouveaux champs
    public String getNewFirstName() {
        return newFirstName;
    }

    public void setNewFirstName(String newFirstName) {
        this.newFirstName = newFirstName;
    }

    public String getNewLastName() {
        return newLastName;
    }

    public void setNewLastName(String newLastName) {
        this.newLastName = newLastName;
    }

    public String getSelectedFirstName() {
        return selectedFirstName;
    }

    public void setSelectedFirstName(String selectedFirstName) {
        this.selectedFirstName = selectedFirstName;
    }

    public String getSelectedLastName() {
        return selectedLastName;
    }

    public void setSelectedLastName(String selectedLastName) {
        this.selectedLastName = selectedLastName;
    }

    public Long getSelectedAuthorId() {
        return selectedAuthorId;
    }

    public void setSelectedAuthorId(Long selectedAuthorId) {
        this.selectedAuthorId = selectedAuthorId;
    }

}
