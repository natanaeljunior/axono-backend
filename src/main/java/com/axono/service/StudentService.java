package com.axono.service;

import com.axono.domain.User;
import com.axono.dto.StudentRequest;
import com.axono.dto.StudentResponse;
import com.axono.dto.StudentUpdateRequest;
import com.axono.exception.BusinessException;
import com.axono.exception.ResourceNotFoundException;
import com.axono.repository.GroupRepository;
import com.axono.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Serviço de alunos - usa User com role ALUNO.
 * StudentService extends BaseServiceImpl -> StudentRepository extends UserRepository extends BaseRepository
 */
@Service
public class StudentService extends BaseServiceImpl<User, StudentRepository> {

    private static final String TEMP_PASSWORD = "ChangeMe@123";

    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;

    public StudentService(StudentRepository repository, PasswordEncoder passwordEncoder, GroupRepository groupRepository) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
        this.groupRepository = groupRepository;
    }

    @Transactional(readOnly = true)
    public Page<StudentResponse> findAll(Pageable pageable) {
        return repository.findAllStudents(pageable)
                .map(StudentResponse::from);
    }

    @Transactional(readOnly = true)
    public StudentResponse getById(UUID id) {
        User user = repository.findStudentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno", id));
        return StudentResponse.from(user);
    }

    @Transactional
    public StudentResponse create(StudentRequest request) {
        validateEmailUnique(request.email(), null);
        validateMatriculaUnique(request.matricula(), null);

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email().trim());
        user.setMatricula(request.matricula().trim());
        user.setRoles(List.of(User.UserRole.ALUNO));

        if (request.hasPassword()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
            user.setFirstAccessPending(false);
        } else {
            user.setPasswordHash(passwordEncoder.encode(TEMP_PASSWORD));
            user.setFirstAccessPending(true);
        }

        if (request.groupId() != null) {
            user.setGroup(groupRepository.findById(request.groupId())
                    .orElseThrow(() -> new ResourceNotFoundException("Grupo", request.groupId())));
        }

        return StudentResponse.from(save(user));
    }

    @Transactional
    public StudentResponse update(UUID id, StudentUpdateRequest request) {
        User user = repository.findStudentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno", id));

        if (request.name() != null && !request.name().isBlank()) {
            user.setName(request.name());
        }
        if (request.email() != null && !request.email().isBlank()) {
            validateEmailUnique(request.email(), id);
            user.setEmail(request.email().trim());
        }
        if (request.matricula() != null && !request.matricula().isBlank()) {
            validateMatriculaUnique(request.matricula(), id);
            user.setMatricula(request.matricula().trim());
        }
        if (request.hasPassword()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
            user.setFirstAccessPending(false);
        }

        if (request.groupId() != null) {
            user.setGroup(groupRepository.findById(request.groupId())
                    .orElseThrow(() -> new ResourceNotFoundException("Grupo", request.groupId())));
        }

        return StudentResponse.from(save(user));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        User user = repository.findStudentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno", id));
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

    private void validateMatriculaUnique(String matricula, UUID excludeId) {
        if (excludeId != null && repository.existsByMatriculaAndIdNot(matricula, excludeId)) {
            throw new BusinessException("Matrícula já cadastrada para outro usuário");
        }
        if (excludeId == null && repository.findByMatricula(matricula).isPresent()) {
            throw new BusinessException("Matrícula já cadastrada");
        }
    }
}
