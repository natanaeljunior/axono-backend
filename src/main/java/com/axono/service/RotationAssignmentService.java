package com.axono.service;

import com.axono.domain.*;
import com.axono.dto.RotationAssignmentResponse;
import com.axono.dto.RotationAssignmentSlotRequest;
import com.axono.exception.BusinessException;
import com.axono.exception.ResourceNotFoundException;
import com.axono.repository.GroupRepository;
import com.axono.repository.HospitalRepository;
import com.axono.repository.PreceptorRepository;
import com.axono.repository.RotationAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RotationAssignmentService {

    private final RotationAssignmentRepository assignmentRepository;
    private final GroupRepository groupRepository;
    private final HospitalRepository hospitalRepository;
    private final PreceptorRepository preceptorRepository;

    @Transactional(readOnly = true)
    public List<RotationAssignmentResponse> findByGroupId(UUID groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", groupId));
        List<RotationAssignment> list = assignmentRepository.findByGroupIdOrderByPeriodIndex(groupId);
        // Garantir 6 itens (period 1-6); slots não criados aparecem como null nos campos
        List<RotationAssignmentResponse> result = new ArrayList<>();
        Map<Integer, RotationAssignment> byPeriod = list.stream()
                .collect(Collectors.toMap(RotationAssignment::getPeriodIndex, Function.identity()));
        for (int p = 1; p <= 6; p++) {
            RotationAssignment a = byPeriod.get(p);
            if (a != null) {
                result.add(RotationAssignmentResponse.from(a));
            } else {
                result.add(emptySlot(groupId, p));
            }
        }
        return result;
    }

    @Transactional
    public List<RotationAssignmentResponse> saveAssignments(UUID groupId, List<RotationAssignmentSlotRequest> slots) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", groupId));
        if (slots == null || slots.isEmpty()) {
            return findByGroupId(groupId);
        }
        for (RotationAssignmentSlotRequest slot : slots) {
            RotationAssignment a = assignmentRepository.findByGroupIdAndPeriodIndex(groupId, slot.periodIndex())
                    .orElseGet(() -> {
                        RotationAssignment newA = new RotationAssignment();
                        newA.setGroup(group);
                        newA.setPeriodIndex(slot.periodIndex());
                        return newA;
                    });
            a.setRotationType(parseRotationType(slot.rotationType()));
            if (slot.hospitalId() != null) {
                a.setHospital(hospitalRepository.findById(slot.hospitalId())
                        .orElseThrow(() -> new ResourceNotFoundException("Hospital", slot.hospitalId())));
            } else {
                a.setHospital(null);
            }
            if (slot.preceptorId() != null) {
                a.setPreceptor(preceptorRepository.findPreceptorById(slot.preceptorId())
                        .orElseThrow(() -> new ResourceNotFoundException("Preceptor", slot.preceptorId())));
            } else {
                a.setPreceptor(null);
            }
            assignmentRepository.save(a);
        }
        return findByGroupId(groupId);
    }

    private static RotationAssignmentResponse emptySlot(UUID groupId, int periodIndex) {
        return new RotationAssignmentResponse(null, groupId, periodIndex, null, null, null, null, null);
    }

    private static RotationType parseRotationType(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return RotationType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Tipo de rotação inválido. Use: SURGERY, PEDIATRICS, CLINICAL, GYNECOLOGY, EMERGENCY, HEALTH");
        }
    }
}
