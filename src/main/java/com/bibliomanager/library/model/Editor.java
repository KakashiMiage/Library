package com.bibliomanager.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "editor")
@NoArgsConstructor
@AllArgsConstructor
public class Editor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long editorId;

    @Column(nullable = false, unique = true, length = 100)
    private String editorName;

    @Column(nullable = false, unique = true, length = 14)
    private Long editorSIRET; // ou Long si tu préfères

    // ManyToMany relation avec Type
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "editor_types",
            joinColumns = @JoinColumn(name = "editor_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    @JsonIgnore
    private List<Type> types;

    @OneToMany(mappedBy = "editor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "editor-book")
    private List<Book> books;

    // Getters & Setters
    public Long getEditorId() {
        return editorId;
    }

    public void setEditorId(Long editorId) {
        this.editorId = editorId;
    }

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public Long getEditorSIRET() {
        return editorSIRET;
    }

    public void setEditorSIRET(Long editorSIRET) {
        this.editorSIRET = editorSIRET;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
