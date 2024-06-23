package edu.yacoubi.usermanagement.service;

import edu.yacoubi.usermanagement.model.Confirmation;
import edu.yacoubi.usermanagement.model.User;

import java.util.Optional;
public interface IConfirmationService {
    String validateToken(String token);
    Optional<Confirmation> findByToken(String token);
    void deleteUserToken(Long id);
}
