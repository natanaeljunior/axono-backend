package com.axono.service;

import com.axono.domain.Group;
import com.axono.domain.RotationAssignment;
import com.axono.domain.User;
import com.axono.dto.GroupRequest;
import com.axono.dto.GroupResponse;
import com.axono.dto.GroupUpdateRequest;
import com.axono.dto.RotationAssignmentResponse;
import com.axono.exception.BusinessException;
import com.axono.exception.ResourceNotFoundException;
import com.axono.repository.GroupRepository;
import com.axono.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final RotationAssignmentService rotationAssignmentService;

    @Transactional(readOnly = true)
    public Page<GroupResponse> findAll(String cycle, Pageable pageable) {
        Page<Group> page = cycle != null && !cycle.isBlank()
                ? groupRepository.findByCycleOrderByCode(cycle.trim(), pageable)
                : groupRepository.findAllByOrderByCode(pageable);
        return page.map(g -> GroupResponse.from(g, userRepository.countByGroupAndRolesContaining(g, User.UserRole.ALUNO)));
    }

    @Transactional(readOnly = true)
    public GroupResponse getById(UUID id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", id));
        long count = userRepository.countByGroupAndRolesContaining(group, User.UserRole.ALUNO);
        List<RotationAssignmentResponse> assignments = rotationAssignmentService.findByGroupId(id);
        return GroupResponse.from(group, count, assignments);
    }

    @Transactional
    public GroupResponse create(GroupRequest request) {
        String cycle = request.cycle() != null && !request.cycle().isBlank() ? request.cycle().trim() : null;
        validateCodeUnique(request.code(), cycle, null);

        Group group = new Group();
        group.setCode(request.code().trim());
        group.setCycle(cycle);
        group = groupRepository.save(group);

        for (int i = 1; i <= 6; i++) {
            RotationAssignment a = new RotationAssignment();
            a.setGroup(group);
            a.setPeriodIndex(i);
            group.getAssignments().add(a);
        }
        groupRepository.save(group);

        return GroupResponse.from(group, 0, rotationAssignmentService.findByGroupId(group.getId()));
    }

    @Transactional
    public GroupResponse update(UUID id, GroupUpdateRequest request) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", id));

        if (request.code() != null && !request.code().isBlank()) {
            validateCodeUnique(request.code(), group.getCycle(), id);
            group.setCode(request.code().trim());
        }
        if (request.cycle() != null) {
            group.setCycle(request.cycle().isBlank() ? null : request.cycle().trim());
        }

        group = groupRepository.save(group);
        long count = userRepository.countByGroupAndRolesContaining(group, User.UserRole.ALUNO);
        List<RotationAssignmentResponse> assignments = rotationAssignmentService.findByGroupId(id);
        return GroupResponse.from(group, count, assignments);
    }

    @Transactional
    public void deleteById(UUID id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", id));
        groupRepository.delete(group);
    }

    private void validateCodeUnique(String code, String cycle, UUID excludeId) {
        if (code == null || code.isBlank()) return;
        String c = code.trim();
        String cy = (cycle != null && !cycle.isBlank()) ? cycle.trim() : null;
        if (cy != null) {
            if (excludeId != null && groupRepository.existsByCycleAndCodeIgnoreCaseAndIdNot(cy, c, excludeId)) {
                throw new BusinessException("Já existe um grupo com este código no ciclo informado");
            }
            if (excludeId == null && groupRepository.existsByCycleAndCodeIgnoreCase(cy, c)) {
                throw new BusinessException("Já existe um grupo com este código no ciclo informado");
            }
        }
    }
}
