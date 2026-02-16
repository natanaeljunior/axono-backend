package com.axono.repository;

import com.axono.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositório de alunos (User com role ALUNO).
 * Estende UserRepository que estende BaseRepository.
 */
@Repository
public interface StudentRepository extends UserRepository {

    default Page<User> findAllStudents(Pageable pageable) {
        return findByRolesContaining(User.UserRole.ALUNO, pageable);
    }

    default Optional<User> findStudentById(UUID id) {
        return findByIdAndRolesContaining(id, User.UserRole.ALUNO);
    }
}
