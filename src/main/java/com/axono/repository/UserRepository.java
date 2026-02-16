package com.axono.repository;

import com.axono.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByMatricula(String matricula);

    Optional<User> findByCrm(String crm);
}
