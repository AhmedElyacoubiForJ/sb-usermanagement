package edu.yacoubi.usermanagement.service;

import edu.yacoubi.usermanagement.model.User;

import java.util.Optional;

public interface IPasswordResetService {
    @Deprecated
    void createPasswordResetTokenForUser(User user, String passwordResetToken);

    String verifyToken(String theToken);

    Optional<User> findUserByPasswordResetToken(String theToken);

    void resetPassword(User user, String password);

    void makeResetPasswordInProcess(User user);

    //String verifyToken(String theToken);
}