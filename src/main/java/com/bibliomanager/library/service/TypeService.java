package com.bibliomanager.library.service;

import com.bibliomanager.library.model.Type;
import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.repository.TypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {

    @Autowired
    private final TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public List<Type> findAllTypes() {
        return (List<Type>) typeRepository.findAll();
    }

    public long countTypes() {
        return typeRepository.count();
    }

    public Type createType(Type type) {
        return typeRepository.save(type);
    }

    public Type getTypeById(Long typeId) {
        return typeRepository.findById(typeId)
                .orElseThrow(() -> new EntityNotFoundException("Type introuvable avec l'id " + typeId));
    }

    public Type updateType(Long typeId, Type updatedType) {
        Type existingType = getTypeById(typeId);

        existingType.setTypeName(updatedType.getTypeName());

        return typeRepository.save(existingType);
    }

    public void deleteType(Long typeId) {
        Type existingType = getTypeById(typeId);
        typeRepository.delete(existingType);
    }

    public Type findTypeByName(String typeName) {
        return typeRepository.findByTypeNameIgnoreCase(typeName)
                .orElseThrow(() -> new EntityNotFoundException("Type non trouvé avec le nom : " + typeName));
    }

    public List<Book> getBooksByType(Long typeId) {
        getTypeById(typeId); // Vérifie que le type existe
        return typeRepository.findBooksByType(typeId);
    }

    public List<Type> getTypesByGenre(Long genreId) {
        return typeRepository.findTypesByGenre(genreId);
    }
}
