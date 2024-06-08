package edu.yacoubi.usermanagement.repository;

import edu.yacoubi.usermanagement.model.PasswordResetConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetConfirmationRepository
        extends JpaRepository<PasswordResetConfirmation, Long> {
    Optional<PasswordResetConfirmation> findByToken(String token);
}
