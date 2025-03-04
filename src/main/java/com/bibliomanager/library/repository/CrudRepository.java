package com.bibliomanager.library.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;


@NoRepositoryBean
/*
* @NoRepositoryBean: Prevents a repository interface from being instantiated as a Spring bean.
* - Used on base or abstract repository interfaces that are not meant to be used directly but are extended by
* other repository interfaces.
* - Prevents Spring from creating proxy beans for the annotated interface during runtime.
*/
public interface CrudRepository<T, ID> extends Repository<T, ID> {

    <S extends T> S save(S entity);

    <S extends T> Iterable<S> saveAll(Iterable<S> entities);

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    Iterable<T> findAll();

    Iterable<T> findAllById(Iterable<ID> ids);

    long count();

    void deleteById(ID id);

    void delete(T entity);

    //void deleteAllById(Iterable<? extends ID> ids);

    void deleteAll(Iterable<? extends T> entities);

    void deleteAll();


}