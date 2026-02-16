package com.axono.repository;

import com.axono.domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

/**
 * Repositório base genérico para acesso a dados.
 * Os repositórios futuros devem estender esta interface.
 *
 * @param <T>  tipo da entidade que estende BaseEntity
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, UUID> {
}
