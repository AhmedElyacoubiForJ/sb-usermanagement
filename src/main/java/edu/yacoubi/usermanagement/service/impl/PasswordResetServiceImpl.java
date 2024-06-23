package edu.yacoubi.usermanagement.service.impl;

import edu.yacoubi.usermanagement.event.UserEvent;
import edu.yacoubi.usermanagement.event.enumeration.EventType;
import edu.yacoubi.usermanagement.model.User;
import edu.yacoubi.usermanagement.service.IPasswordResetService;
import edu.yacoubi.usermanagement.model.PasswordResetConfirmation;
import edu.yacoubi.usermanagement.repository.PasswordResetConfirmationRepository;
import edu.yacoubi.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Map;
import java.util.Optional;

import static edu.yacoubi.usermanagement.constants.TokenStatus.INVALID;
import static edu.yacoubi.usermanagement.constants.TokenStatus.VALID;
import static edu.yacoubi.usermanagement.constants.TokenStatus.EXPIRED;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetServiceImpl implements IPasswordResetService {
    private final PasswordResetConfirmationRepository passwordResetConfirmationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;

    @Override
    public String verifyToken(String theToken) {
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
        passwordResetToken.get().setVerified(true);
        passwordResetConfirmationRepository.save(passwordResetToken.get());
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

    @Override
    public void makeResetPasswordInProcess(User user) {
        log.info("PasswordResetService make reset password for {} in process", user.getEmail());

        PasswordResetConfirmation confirmationToken = new PasswordResetConfirmation(user);

        log.info("Saving a password reset, token confirmation for user: {}", user.getEmail());
        passwordResetConfirmationRepository.save(confirmationToken);

        log.info("Publish user event type: {} for sending a email confirmation", EventType.RESET_PASSWORD);
        publisher.publishEvent(
                new UserEvent(
                        confirmationToken.getUser(),
                        EventType.RESET_PASSWORD,
                        Map.of("token", confirmationToken.getToken())
                )
        );

        log.info("Password reset for User '{}' is successfully requested", user.getEmail());
    }
}
