package edu.yacoubi.usermanagement.service;

import edu.yacoubi.usermanagement.model.User;
import edu.yacoubi.usermanagement.controller.dto.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    User registerUser(RegistrationRequest registrationRequest);
    void updateUser(Long id, String firstName, String lastName, String email);
    //
    void createUser(String firstName, String lastName, String email, String password);
}
