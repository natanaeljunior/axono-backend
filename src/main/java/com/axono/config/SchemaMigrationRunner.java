package com.axono.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Remove a coluna antiga {@code role} da tabela {@code users} quando existir,
 * após a migração para múltiplas permissões (tabela user_roles).
 * Roda logo após o Hibernate criar/atualizar o schema e antes dos seeds.
 */
@Component
@DependsOn("entityManagerFactory")
@RequiredArgsConstructor
@Slf4j
public class SchemaMigrationRunner {

    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void dropOldRoleColumn() {
        try {
            jdbcTemplate.execute("ALTER TABLE users DROP COLUMN IF EXISTS role");
            log.info("Schema migration: coluna 'role' removida de 'users' (se existia).");
        } catch (Exception e) {
            log.warn("Schema migration (drop role): {} - ignorando se a coluna já não existir.", e.getMessage());
        }
    }
}
