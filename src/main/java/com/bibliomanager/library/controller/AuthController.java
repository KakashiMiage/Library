package com.bibliomanager.library.controller;

import com.bibliomanager.library.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> loginData) {
        boolean success = authService.login(loginData.get("username"), loginData.get("password"));
        return success ? ResponseEntity.ok("Connexion réussie !") : ResponseEntity.status(401).body("Mauvais identifiants");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        authService.logout();
        return ResponseEntity.ok("Déconnexion réussie !");
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        if (authService.isLoggedIn()) {
            return ResponseEntity.ok("Utilisateur connecté : " + authService.getCurrentUsername());
        }
        return ResponseEntity.status(401).body("Aucun utilisateur connecté");
    }
}
