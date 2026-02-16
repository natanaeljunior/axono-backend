package com.axono.controller;

import com.axono.dto.HospitalRequest;
import com.axono.dto.HospitalResponse;
import com.axono.dto.HospitalUpdateRequest;
import com.axono.service.HospitalService;
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
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping
    public ResponseEntity<Page<HospitalResponse>> findAll(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(hospitalService.findAll(name, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(hospitalService.getById(id));
    }

    @PostMapping
    public ResponseEntity<HospitalResponse> create(@Valid @RequestBody HospitalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hospitalService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HospitalResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody HospitalUpdateRequest request) {
        return ResponseEntity.ok(hospitalService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        hospitalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
