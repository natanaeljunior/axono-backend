package com.axono.service;

import com.axono.domain.BaseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface base de serviço com operações CRUD genéricas.
 * Os serviços futuros devem estender esta interface.
 *
 * @param <T> tipo da entidade
 */
public interface BaseService<T extends BaseEntity> {

    Optional<T> findById(UUID id);

    List<T> findAll();

    T save(T entity);

    void deleteById(UUID id);

    boolean existsById(UUID id);
}
