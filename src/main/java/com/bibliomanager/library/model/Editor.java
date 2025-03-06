package com.bibliomanager.library.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "editor")
public class Editor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer editorId;

    @Column(nullable = false, unique = true)
    private String editorName;

    @Column(nullable = false)
    private Double editorSIRET;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    @JsonBackReference
    private Type editorType;

    @OneToMany(mappedBy = "editor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books;

    public Editor() {
    }

    public Editor(String editorName, Double editorSIRET, Type editorType) {
        this.editorName = editorName;
        this.editorSIRET = editorSIRET;
        this.editorType = editorType;
    }

    public Integer getEditorId() {
        return editorId;
    }

    public void setEditorId(Integer editorId) {
        this.editorId = editorId;
    }

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public Double getEditorSIRET() {
        return editorSIRET;
    }

    public void setEditorSIRET(Double editorSIRET) {
        this.editorSIRET = editorSIRET;
    }

    public Type getEditorType() {
        return editorType;
    }

    public void setEditorType(Type editorType) {
        this.editorType = editorType;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}

