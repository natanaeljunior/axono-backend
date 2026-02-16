package com.axono.repository;

import com.axono.domain.RotationAssignment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositório de alocações da matriz de rotações (uma por grupo por período 1-6).
 */
public interface RotationAssignmentRepository extends BaseRepository<RotationAssignment> {

    List<RotationAssignment> findByGroupIdOrderByPeriodIndex(UUID groupId);

    Optional<RotationAssignment> findByGroupIdAndPeriodIndex(UUID groupId, int periodIndex);

    void deleteByGroupId(UUID groupId);
}
