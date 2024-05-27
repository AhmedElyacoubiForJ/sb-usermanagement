package edu.yacoubi.usermanagement.service.impl;

import edu.yacoubi.usermanagement.model.Confirmation;
import edu.yacoubi.usermanagement.repository.ConfirmationRepository;
import edu.yacoubi.usermanagement.model.User;
import edu.yacoubi.usermanagement.enumeration.EventType;
import edu.yacoubi.usermanagement.event.UserEvent;
import edu.yacoubi.usermanagement.controller.dto.RegistrationRequest;
import edu.yacoubi.usermanagement.repository.UserRepository;
import edu.yacoubi.usermanagement.model.Role;
import edu.yacoubi.usermanagement.repository.RoleRepository;
import edu.yacoubi.usermanagement.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ConfirmationRepository confirmationRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;

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
        Optional<Role> userRole = roleRepository.findByNameIgnoreCase("ROLE_USER");
        var user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
                //Arrays.asList(new Role("USER_ROLE")) // the registered user has per default USER_ROLE
        );
        user.setRoles(Arrays.asList(userRole.get()));
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

    @Override
    public void createUser(String firstName, String lastName, String email, String password) {
        Optional<Role> userRole = roleRepository.findByNameIgnoreCase("ROLE_USER");
        var user = new User(
                firstName,
                lastName,
                email,
                passwordEncoder.encode(password)
                // Arrays.asList(new Role("USER_ROLE")) // the registered user has per default USER_ROLE
        );
        user.setRoles(Arrays.asList(userRole.get()));
        User savedUser = userRepository.save(user);
        Confirmation confirmation = new Confirmation(savedUser);
        confirmationRepository.save(confirmation);
        publisher.publishEvent(
                new UserEvent(
                        confirmation.getUser(),
                        EventType.REGISTRATION,
                        Map.of("token", confirmation.getToken())
                )
        );
    }
}
