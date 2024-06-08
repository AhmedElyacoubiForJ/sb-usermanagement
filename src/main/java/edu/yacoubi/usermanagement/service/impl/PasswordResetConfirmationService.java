package edu.yacoubi.usermanagement.service.impl;

import edu.yacoubi.usermanagement.model.User;
import edu.yacoubi.usermanagement.service.IPasswordResetConfirmationService;
import edu.yacoubi.usermanagement.model.PasswordResetConfirmation;
import edu.yacoubi.usermanagement.repository.PasswordResetConfirmationRepository;
import edu.yacoubi.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

import static edu.yacoubi.usermanagement.constants.TokenStatus.INVALID;
import static edu.yacoubi.usermanagement.constants.TokenStatus.VALID;
import static edu.yacoubi.usermanagement.constants.TokenStatus.EXPIRED;

@Service
@RequiredArgsConstructor
public class PasswordResetConfirmationService implements IPasswordResetConfirmationService {
    private final PasswordResetConfirmationRepository passwordResetConfirmationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createPasswordResetTokenForUser(User user, String passwordResetToken) {
        PasswordResetConfirmation newToken =
                new PasswordResetConfirmation(passwordResetToken, user);
        passwordResetConfirmationRepository.save(newToken);
    }

    @Override
    public String validatePasswordResetToken(String theToken) {
        Optional<PasswordResetConfirmation> passwordResetToken =
                passwordResetConfirmationRepository.findByToken(theToken);
        if (passwordResetToken.isEmpty()) {
            return INVALID;
        }
        Calendar calendar = Calendar.getInstance();
        if ((passwordResetToken.get().getExpirationTime().getTime()
                        - calendar.getTime().getTime()) <= 0) {
            return EXPIRED;
        }
        return VALID;
    }

    @Override
    public Optional<User> findUserByPasswordResetToken(String theToken) {
        return Optional.ofNullable(
                passwordResetConfirmationRepository
                        .findByToken(theToken)
                        .get()
                        .getUser()
        );
    }

    @Override
    public void resetPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
