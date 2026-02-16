package com.axono.service;

import com.axono.domain.Hospital;
import com.axono.dto.HospitalRequest;
import com.axono.dto.HospitalResponse;
import com.axono.dto.HospitalUpdateRequest;
import com.axono.exception.BusinessException;
import com.axono.exception.ResourceNotFoundException;
import com.axono.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    @Transactional(readOnly = true)
    public Page<HospitalResponse> findAll(String name, Pageable pageable) {
        if (name != null && !name.isBlank()) {
            return hospitalRepository.findByNameContainingIgnoreCase(name.trim(), pageable)
                    .map(HospitalResponse::from);
        }
        return hospitalRepository.findAllByOrderByName(pageable)
                .map(HospitalResponse::from);
    }

    @Transactional(readOnly = true)
    public HospitalResponse getById(UUID id) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital", id));
        return HospitalResponse.from(hospital);
    }

    @Transactional
    public HospitalResponse create(HospitalRequest request) {
        Hospital hospital = new Hospital();
        mapRequestToEntity(request, hospital);
        hospital = hospitalRepository.save(hospital);
        return HospitalResponse.from(hospital);
    }

    @Transactional
    public HospitalResponse update(UUID id, HospitalUpdateRequest request) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital", id));
        if (request.name() != null && !request.name().isBlank()) {
            hospital.setName(request.name().trim());
        }
        if (request.address() != null) {
            hospital.setAddress(trimOrNull(request.address()));
        }
        if (request.status() != null && !request.status().isBlank()) {
            hospital.setStatus(parseStatus(request.status()));
        }
        if (request.directorName() != null) {
            hospital.setDirectorName(trimOrNull(request.directorName()));
        }
        if (request.directorEmail() != null) {
            hospital.setDirectorEmail(trimOrNull(request.directorEmail()));
        }
        if (request.capacity() != null) {
            hospital.setCapacity(request.capacity());
        }
        if (request.conventionExpiresAt() != null) {
            hospital.setConventionExpiresAt(request.conventionExpiresAt());
        }
        if (request.tag() != null) {
            hospital.setTag(trimOrNull(request.tag()));
        }
        hospital = hospitalRepository.save(hospital);
        return HospitalResponse.from(hospital);
    }

    @Transactional
    public void deleteById(UUID id) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital", id));
        hospitalRepository.delete(hospital);
    }

    private static void mapRequestToEntity(HospitalRequest request, Hospital hospital) {
        hospital.setName(request.name().trim());
        hospital.setAddress(trimOrNull(request.address()));
        hospital.setStatus(request.status() != null && !request.status().isBlank() ? parseStatus(request.status()) : Hospital.HospitalStatus.ACTIVE);
        hospital.setDirectorName(trimOrNull(request.directorName()));
        hospital.setDirectorEmail(trimOrNull(request.directorEmail()));
        hospital.setCapacity(request.capacity());
        hospital.setConventionExpiresAt(request.conventionExpiresAt());
        hospital.setTag(trimOrNull(request.tag()));
    }

    private static Hospital.HospitalStatus parseStatus(String value) {
        try {
            return Hospital.HospitalStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Status inválido. Use: ACTIVE, EXPIRING, INACTIVE");
        }
    }

    private static String trimOrNull(String s) {
        if (s == null || s.isBlank()) return null;
        return s.trim();
    }
}
