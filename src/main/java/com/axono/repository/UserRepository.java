package com.axono.repository;

import com.axono.domain.Group;
import com.axono.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByMatricula(String matricula);

    Optional<User> findByCrm(String crm);

    Page<User> findByRolesContaining(User.UserRole role, Pageable pageable);

    Optional<User> findByIdAndRolesContaining(UUID id, User.UserRole role);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, UUID id);

    boolean existsByMatriculaAndIdNot(String matricula, UUID id);

    boolean existsByCrmAndIdNot(String crm, UUID id);

    long countByGroupAndRolesContaining(Group group, User.UserRole role);
}
