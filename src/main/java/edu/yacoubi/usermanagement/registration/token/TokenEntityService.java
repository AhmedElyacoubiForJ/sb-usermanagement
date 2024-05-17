package edu.yacoubi.usermanagement.registration.token;

import edu.yacoubi.usermanagement.user.User;
import edu.yacoubi.usermanagement.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class TokenEntityService implements ITokenEntityService {
    private final TokenEntityRepository tokenEntityRepository;
    private final UserRepository userRepository;

    @Override
    public String validateToken(String token) {
        Optional<TokenEntity> theToken = tokenEntityRepository.findByToken(token);
        if (theToken.isEmpty()) {
            return "INVALID";
        }
        User user = theToken.get().getUser();
        Calendar calendar = Calendar.getInstance();
        if ((theToken.get().getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            //deleteUserToken(user.getId());
            return "EXPIRED";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "VALID";
    }

    @Override
    public void saveTokenForUser(String token, User user) {
        var verificationToken = new TokenEntity(token, user);
        tokenEntityRepository.save(verificationToken);
    }

    @Override
    public Optional<TokenEntity> findByToken(String token) {
        return tokenEntityRepository.findByToken(token);
    }

    @Override
    public void deleteUserToken(Long id) {
        tokenEntityRepository.deleteByUserId(id);
    }
}
