package com.bibliomanager.library.service;

import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.model.Type;
import com.bibliomanager.library.repository.TypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeService {

    private final TypeRepository typeRepository;
    private final AuthService authService;

    public TypeService(TypeRepository typeRepository, AuthService authService) {
        this.typeRepository = typeRepository;
        this.authService = authService;
    }

    public Iterable<Type> getAllTypes() {
        if (!authService.isLoggedIn()) {
                throw new RuntimeException("You need to be logged");
        }
        return this.typeRepository.findAll();
    }

    public long countTypes() {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.typeRepository.countTypes();
    }

    public List<Type> findTypeByName(String typeName) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.typeRepository.findTypeByName(typeName);
    }

    public Type createType(Type type) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.typeRepository.save(type);
    }

    public Optional<Type> getTypeById(Long typeId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.typeRepository.findById(typeId);
    }

    public Type updateType(Long typeId, Type updatedType) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        if (typeRepository.existsById(typeId)) {
            updatedType.setTypeId(typeId);
            return typeRepository.save(updatedType);
        }
        return null;
    }

    public void deleteType(Long typeId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        this.typeRepository.deleteById(typeId);
    }

    public List<Book> getBooksByType(Long typeId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.typeRepository.getBooksByType(typeId);
    }
}
