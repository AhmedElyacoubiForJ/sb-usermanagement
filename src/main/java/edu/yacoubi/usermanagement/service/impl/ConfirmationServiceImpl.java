package edu.yacoubi.usermanagement.service.impl;

import edu.yacoubi.usermanagement.model.Confirmation;
import edu.yacoubi.usermanagement.model.User;
import edu.yacoubi.usermanagement.repository.ConfirmationRepository;
import edu.yacoubi.usermanagement.repository.UserRepository;
import edu.yacoubi.usermanagement.service.IConfirmationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;


import static edu.yacoubi.usermanagement.constants.TokenStatus.INVALID;
import static edu.yacoubi.usermanagement.constants.TokenStatus.VALID;
import static edu.yacoubi.usermanagement.constants.TokenStatus.EXPIRED;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmationServiceImpl implements IConfirmationService {
    private final ConfirmationRepository confirmationRepository;
    private final UserRepository userRepository;

    @Override
    public String validateToken(String token) {
        log.info("ConfirmationService to validate token: {}", token);

        Optional<Confirmation> confirmationOptional = confirmationRepository.findByToken(token);

        if (confirmationOptional.isEmpty()) {
            log.info("Token: {} is invalid", token);
            return INVALID;
        }
        if (isTokenExpired(confirmationOptional.get())) {
            log.info("Token: {} is expired", token);
            return EXPIRED;
        }

        User user = confirmationOptional.get().getUser();
        user.setEnabled(true);
        userRepository.save(user);

        log.info("Token: {} successfully validated for username {}", token,user.getEmail());

        return VALID;
    }

    @Override
    public Optional<Confirmation> findByToken(String token) {
        return confirmationRepository.findByToken(token);
    }

    @Override
    public void deleteUserToken(Long id) {
        confirmationRepository.deleteByUserId(id);
    }



    private boolean isTokenExpired(Confirmation confirmation) {
        Calendar calendar = Calendar.getInstance();
        return (confirmation.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0;
    }
}
