package edu.yacoubi.usermanagement.config;

import edu.yacoubi.usermanagement.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAppDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
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
