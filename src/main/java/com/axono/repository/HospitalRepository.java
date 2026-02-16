package com.axono.repository;

import com.axono.domain.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Repositório de hospitais / unidades de estágio.
 */
public interface HospitalRepository extends BaseRepository<Hospital> {

    Page<Hospital> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Hospital> findAllByOrderByName(Pageable pageable);
}
