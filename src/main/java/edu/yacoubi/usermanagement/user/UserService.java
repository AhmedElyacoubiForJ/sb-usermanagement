package edu.yacoubi.usermanagement.user;

import edu.yacoubi.usermanagement.registration.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public User registerUser(RegistrationRequest registrationRequest) {
        return null;
    }

    @Override
    public void updateUser(Long id, String firstName, String lastName, String email) {

    }
}
