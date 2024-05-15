package edu.yacoubi.usermanagement.security;

import edu.yacoubi.usermanagement.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAppDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return userRepository
                .findByEmail(email)
                .map(UserAppDetails::new)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "User with email " + email + " could not be found"
                        )
                );
    }
}
