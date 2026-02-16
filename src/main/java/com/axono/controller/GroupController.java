package com.axono.controller;

import com.axono.dto.GroupRequest;
import com.axono.dto.GroupResponse;
import com.axono.dto.GroupUpdateRequest;
import com.axono.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<Page<GroupResponse>> findAll(
            @RequestParam(required = false) String cycle,
            @PageableDefault(size = 20, sort = "code") Pageable pageable) {
        return ResponseEntity.ok(groupService.findAll(cycle, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(groupService.getById(id));
    }

    @PostMapping
    public ResponseEntity<GroupResponse> create(@Valid @RequestBody GroupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody GroupUpdateRequest request) {
        return ResponseEntity.ok(groupService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        groupService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
