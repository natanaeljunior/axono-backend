package com.axono.controller;

import com.axono.dto.PreceptorRequest;
import com.axono.dto.PreceptorResponse;
import com.axono.dto.PreceptorUpdateRequest;
import com.axono.service.PreceptorService;
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
@RequestMapping("/api/preceptors")
@RequiredArgsConstructor
public class PreceptorController {

    private final PreceptorService preceptorService;

    @GetMapping
    public ResponseEntity<Page<PreceptorResponse>> findAll(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(preceptorService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PreceptorResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(preceptorService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PreceptorResponse> create(@Valid @RequestBody PreceptorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(preceptorService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreceptorResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody PreceptorUpdateRequest request) {
        return ResponseEntity.ok(preceptorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        preceptorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
