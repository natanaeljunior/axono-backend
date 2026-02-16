package com.axono.repository;

import com.axono.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositório de preceptores (User com role PRECEPTOR).
 * Estende UserRepository que estende BaseRepository.
 */
@Repository
public interface PreceptorRepository extends UserRepository {

    default Page<User> findAllPreceptors(Pageable pageable) {
        return findByRolesContaining(User.UserRole.PRECEPTOR, pageable);
    }

    default Optional<User> findPreceptorById(UUID id) {
        return findByIdAndRolesContaining(id, User.UserRole.PRECEPTOR);
    }
}
