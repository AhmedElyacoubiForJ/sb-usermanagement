package edu.yacoubi.usermanagement.user;

import edu.yacoubi.usermanagement.registration.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User could not be found"))
        );
    }

    @Override
    public Optional<User> findById(Long id) {
       return Optional.ofNullable(userRepository
               .findById(id)
               .orElseThrow(
                       () -> new RuntimeException("User could not be found")
               ));
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
        var user = userRepository.findById(id)
               .orElseThrow(
                        () -> new RuntimeException("User could not be found")
                );
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        userRepository.save(user);
    }
}
