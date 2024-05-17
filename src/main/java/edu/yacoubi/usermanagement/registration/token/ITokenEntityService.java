package edu.yacoubi.usermanagement.registration.token;

import edu.yacoubi.usermanagement.user.User;

import java.util.Optional;
public interface ITokenEntityService {
    String validateToken(String token);
    void saveTokenForUser(String token, User user);
    Optional<TokenEntity> findByToken(String token);
    void deleteUserToken(Long id);
}
