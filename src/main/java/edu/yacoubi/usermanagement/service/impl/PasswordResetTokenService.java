package edu.yacoubi.usermanagement.service.impl;

import edu.yacoubi.usermanagement.model.User;
import edu.yacoubi.usermanagement.service.IPasswordResetTokenService;
import edu.yacoubi.usermanagement.model.PasswordResetToken;
import edu.yacoubi.usermanagement.repository.PasswordResetTokenRepository;
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
public class PasswordResetTokenService  implements IPasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createPasswordResetTokenForUser(User user, String passwordResetToken) {
        PasswordResetToken newToken =
                new PasswordResetToken(passwordResetToken, user);
        passwordResetTokenRepository.save(newToken);
    }

    @Override
    public String validatePasswordResetToken(String theToken) {
        Optional<PasswordResetToken> passwordResetToken =
                passwordResetTokenRepository.findByToken(theToken);
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
                passwordResetTokenRepository
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
