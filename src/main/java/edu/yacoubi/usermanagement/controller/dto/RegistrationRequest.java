package edu.yacoubi.usermanagement.controller.dto;

import edu.yacoubi.usermanagement.model.Role;
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
