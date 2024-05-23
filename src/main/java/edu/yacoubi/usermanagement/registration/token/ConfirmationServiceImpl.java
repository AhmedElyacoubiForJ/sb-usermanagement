package edu.yacoubi.usermanagement.registration.token;

import edu.yacoubi.usermanagement.user.User;
import edu.yacoubi.usermanagement.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

import static edu.yacoubi.usermanagement.utility.TokenUtility.*;

@Service
@RequiredArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {
    private final ConfirmationRepository confirmationRepository;
    private final UserRepository userRepository;

    @Override
    public String validateToken(String token) {
        Optional<Confirmation> confirmationOptional = confirmationRepository.findByToken(token);

        if (confirmationOptional.isEmpty()) return INVALID;
        if (isTokenExpired(confirmationOptional.get())) return EXPIRED;

        User user = confirmationOptional.get().getUser();
        user.setEnabled(true);
        userRepository.save(user);

        return VALID;
    }

    @Override
    public void saveTokenForUser(String token, User user) {
        Confirmation confirmation = new Confirmation(token, user);
        confirmationRepository.save(confirmation);
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
