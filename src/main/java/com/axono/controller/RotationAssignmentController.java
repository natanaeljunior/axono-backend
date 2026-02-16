package com.axono.controller;

import com.axono.dto.RotationAssignmentResponse;
import com.axono.dto.RotationAssignmentSlotRequest;
import com.axono.service.RotationAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Matriz de rotações: alocações por grupo e período (1-6).
 * GET retorna os 6 slots; PUT atualiza em lote.
 */
@RestController
@RequestMapping("/api/groups/{groupId}/assignments")
@RequiredArgsConstructor
public class RotationAssignmentController {

    private final RotationAssignmentService rotationAssignmentService;

    @GetMapping
    public ResponseEntity<List<RotationAssignmentResponse>> getAssignments(@PathVariable UUID groupId) {
        return ResponseEntity.ok(rotationAssignmentService.findByGroupId(groupId));
    }

    @PutMapping
    public ResponseEntity<List<RotationAssignmentResponse>> saveAssignments(
            @PathVariable UUID groupId,
            @Valid @RequestBody List<RotationAssignmentSlotRequest> slots) {
        return ResponseEntity.ok(rotationAssignmentService.saveAssignments(groupId, slots));
    }
}
