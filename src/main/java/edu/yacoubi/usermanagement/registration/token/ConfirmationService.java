package edu.yacoubi.usermanagement.registration.token;

import edu.yacoubi.usermanagement.user.User;

import java.util.Optional;
public interface ConfirmationService {
    String validateToken(String token);
    void saveTokenForUser(String token, User user);
    Optional<Confirmation> findByToken(String token);
    void deleteUserToken(Long id);
}
