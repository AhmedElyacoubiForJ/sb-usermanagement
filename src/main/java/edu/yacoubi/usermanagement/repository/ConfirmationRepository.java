package edu.yacoubi.usermanagement.repository;

import edu.yacoubi.usermanagement.model.Confirmation;
import edu.yacoubi.usermanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {
    Optional<Confirmation> findByToken(String token);
    Optional<Confirmation> findByUser(User user);
    void deleteByUserId(Long id);
}
