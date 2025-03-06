package com.bibliomanager.library.controller;

import com.bibliomanager.library.service.EditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EditorController {

    @Autowired
    private final EditorService editorService;

    public EditorController(EditorService editorService) {
        this.editorService = editorService;
    }
}
