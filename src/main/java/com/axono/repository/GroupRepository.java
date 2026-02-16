package com.axono.repository;

import com.axono.domain.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositório de grupos de internato.
 */
public interface GroupRepository extends BaseRepository<Group> {

    Page<Group> findByCycleOrderByCode(String cycle, Pageable pageable);

    Page<Group> findAllByOrderByCode(Pageable pageable);

    Optional<Group> findByCycleAndCodeIgnoreCase(String cycle, String code);

    boolean existsByCycleAndCodeIgnoreCaseAndIdNot(String cycle, String code, UUID excludeId);

    boolean existsByCycleAndCodeIgnoreCase(String cycle, String code);
}
