package com.axono.service;

import com.axono.domain.User;
import com.axono.dto.PreceptorRequest;
import com.axono.dto.PreceptorResponse;
import com.axono.dto.PreceptorUpdateRequest;
import com.axono.exception.BusinessException;
import com.axono.exception.ResourceNotFoundException;
import com.axono.repository.PreceptorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Serviço de preceptores - usa User com role PRECEPTOR.
 * PreceptorService extends BaseServiceImpl -> PreceptorRepository extends UserRepository extends BaseRepository
 */
@Service
public class PreceptorService extends BaseServiceImpl<User, PreceptorRepository> {

    private static final String TEMP_PASSWORD = "ChangeMe@123";

    private final PasswordEncoder passwordEncoder;

    public PreceptorService(PreceptorRepository repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<PreceptorResponse> findAll(Pageable pageable) {
        return repository.findAllPreceptors(pageable)
                .map(PreceptorResponse::from);
    }

    @Transactional(readOnly = true)
    public PreceptorResponse getById(UUID id) {
        User user = repository.findPreceptorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Preceptor", id));
        return PreceptorResponse.from(user);
    }

    @Transactional
    public PreceptorResponse create(PreceptorRequest request) {
        validateEmailUnique(request.email(), null);
        validateCrmUnique(request.crm(), null);

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email().trim());
        user.setCrm(request.crm().trim());
        user.setRoles(List.of(User.UserRole.PRECEPTOR));

        if (request.hasPassword()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
            user.setFirstAccessPending(false);
        } else {
            user.setPasswordHash(passwordEncoder.encode(TEMP_PASSWORD));
            user.setFirstAccessPending(true);
        }

        return PreceptorResponse.from(save(user));
    }

    @Transactional
    public PreceptorResponse update(UUID id, PreceptorUpdateRequest request) {
        User user = repository.findPreceptorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Preceptor", id));

        if (request.name() != null && !request.name().isBlank()) {
            user.setName(request.name());
        }
        if (request.email() != null && !request.email().isBlank()) {
            validateEmailUnique(request.email(), id);
            user.setEmail(request.email().trim());
        }
        if (request.crm() != null && !request.crm().isBlank()) {
            validateCrmUnique(request.crm(), id);
            user.setCrm(request.crm().trim());
        }
        if (request.hasPassword()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
            user.setFirstAccessPending(false);
        }

        return PreceptorResponse.from(save(user));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        User user = repository.findPreceptorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Preceptor", id));
        super.deleteById(id);
    }

    private void validateEmailUnique(String email, UUID excludeId) {
        if (excludeId != null && repository.existsByEmailIgnoreCaseAndIdNot(email, excludeId)) {
            throw new BusinessException("E-mail já cadastrado para outro usuário");
        }
        if (excludeId == null && repository.findByEmailIgnoreCase(email).isPresent()) {
            throw new BusinessException("E-mail já cadastrado");
        }
    }

    private void validateCrmUnique(String crm, UUID excludeId) {
        if (excludeId != null && repository.existsByCrmAndIdNot(crm, excludeId)) {
            throw new BusinessException("CRM já cadastrado para outro usuário");
        }
        if (excludeId == null && repository.findByCrm(crm).isPresent()) {
            throw new BusinessException("CRM já cadastrado");
        }
    }
}
