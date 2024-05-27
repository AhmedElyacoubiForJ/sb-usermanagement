package edu.yacoubi.usermanagement.service;

import edu.yacoubi.usermanagement.model.Confirmation;
import edu.yacoubi.usermanagement.model.User;

import java.util.Optional;
public interface ConfirmationService {
    String validateToken(String token);
    void saveTokenForUser(String token, User user);
    Optional<Confirmation> findByToken(String token);
    void deleteUserToken(Long id);
}