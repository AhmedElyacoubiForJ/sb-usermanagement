package edu.yacoubi.usermanagement.user;

import edu.yacoubi.usermanagement.registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getAllUsers();
    User findByEmail(String email);
    User findById(Long id);
    User registerUser(RegistrationRequest registrationRequest);
    void updateUser(Long id, String firstName, String lastName, String email);
}
