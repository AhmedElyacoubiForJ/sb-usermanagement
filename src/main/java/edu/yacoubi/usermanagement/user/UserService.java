package edu.yacoubi.usermanagement.user;

import edu.yacoubi.usermanagement.registration.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User with email " + email + " could not be found")
                );
    }

    @Override
    public User findById(Long id) {
       return userRepository
               .findById(id)
               .orElseThrow(
                       () -> new RuntimeException("User with ID: " + id + " could not be found")
    );
    }

    @Override
    public User registerUser(RegistrationRequest request) {
        var user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                Arrays.asList(new Role("USER_ROLE")) // the registered user has per default USER_ROLE
        );
        return userRepository.save(user);
    }

    @Override
    public void updateUser(Long id, String firstName, String lastName, String email) {

    }
}
