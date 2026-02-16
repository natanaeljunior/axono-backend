package com.axono.service;

import com.axono.domain.BaseEntity;
import com.axono.repository.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementação base do serviço com operações CRUD genéricas.
 * Os serviços futuros devem estender esta classe e passar o repositório correspondente.
 *
 * @param <T> tipo da entidade
 * @param <R> tipo do repositório que estende BaseRepository
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
public abstract class BaseServiceImpl<T extends BaseEntity, R extends BaseRepository<T>>
        implements BaseService<T> {

    protected final R repository;

    @Override
    public Optional<T> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}
