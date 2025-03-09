package com.bibliomanager.library.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(nullable = false, unique = true)
    private String editorName;

    @Column(nullable = false)
    private Double editorSIRET;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    @JsonBackReference
    private Type editorType;

    @JsonIgnore
    @OneToMany(mappedBy = "editor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books;

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

