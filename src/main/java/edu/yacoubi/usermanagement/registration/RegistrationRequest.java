package edu.yacoubi.usermanagement.registration;

import edu.yacoubi.usermanagement.role.Role;
import lombok.Data;

import java.util.List;

@Data
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean isEnabled = false;
    private List<Role> roles;
}
