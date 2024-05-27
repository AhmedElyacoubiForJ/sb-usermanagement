package edu.yacoubi.usermanagement.service;

import edu.yacoubi.usermanagement.model.User;

import java.util.Optional;

public interface IPasswordResetTokenService {
    void createPasswordResetTokenForUser(User user, String passwordResetToken);

    String validatePasswordResetToken(String theToken);

    Optional<User> findUserByPasswordResetToken(String theToken);

    void resetPassword(User user, String password);
}
