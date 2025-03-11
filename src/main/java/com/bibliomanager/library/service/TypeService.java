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

    // Récupérer tous les types
    public List<Type> findAllTypes() {
        return (List<Type>) typeRepository.findAll();
    }

    // Compter les types
    public long countTypes() {
        return typeRepository.count();
    }

    // Créer un type
    public Type createType(Type type) {
        return typeRepository.save(type);
    }

    // Trouver un type par ID
    public Type getTypeById(Long typeId) {
        return typeRepository.findById(typeId)
                .orElseThrow(() -> new EntityNotFoundException("Type introuvable avec l'id " + typeId));
    }

    // Mettre à jour un type
    public Type updateType(Long typeId, Type updatedType) {
        Type existingType = getTypeById(typeId);

        existingType.setTypeName(updatedType.getTypeName());

        return typeRepository.save(existingType);
    }

    // Supprimer un type
    public void deleteType(Long typeId) {
        Type existingType = getTypeById(typeId);
        typeRepository.delete(existingType);
    }

    // Trouver un type par son nom
    public Type findTypeByName(String typeName) {
        return typeRepository.findByTypeNameIgnoreCase(typeName)
                .orElseThrow(() -> new EntityNotFoundException("Type non trouvé avec le nom : " + typeName));
    }

    // Récupérer tous les livres d'un type
    public List<Book> getBooksByType(Long typeId) {
        getTypeById(typeId); // Vérifie que le type existe
        return typeRepository.findBooksByType(typeId);
    }

    // Optionnel : récupérer les types liés à un genre spécifique
    public List<Type> getTypesByGenre(Long genreId) {
        return typeRepository.findTypesByGenre(genreId);
    }
}
