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

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public Iterable<Type> getAllTypes() {
        return this.typeRepository.findAll();
    }

    public long countTypes() {
        return this.typeRepository.countTypes();
    }

    public List<Type> findTypeByName(String typeName) {
        return this.typeRepository.findTypeByName(typeName);
    }

    public Type createType(Type type) {
        return this.typeRepository.save(type);
    }

    public Optional<Type> getTypeById(Integer typeId) {
        return this.typeRepository.findById(typeId);
    }

    public Type updateType(Integer typeId, Type updatedType) {
        if (typeRepository.existsById(typeId)) {
            updatedType.setTypeId(typeId);
            return typeRepository.save(updatedType);
        }
        return null;
    }

    public void deleteType(Integer typeId) {
        this.typeRepository.deleteById(typeId);
    }

    public List<Book> getBooksByType(Long typeId) {
        return this.typeRepository.getBooksByType(typeId);
    }
}
