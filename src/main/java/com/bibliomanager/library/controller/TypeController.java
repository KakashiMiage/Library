package com.bibliomanager.library.controller;

import com.bibliomanager.library.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TypeController {

    @Autowired
    private final TypeService typeService;

    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }
}
