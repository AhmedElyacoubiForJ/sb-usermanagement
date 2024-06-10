package edu.yacoubi.usermanagement.config;

import edu.yacoubi.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
// is responsible for loading user data from the database
// and providing it to the Spring Security Framework for
// authentication and authorization purposes.
// requirement by Spring Security is to implement UserDetailsService
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Loading user details for email: {}", email); // Debug log

        return userRepository.findByEmail(email)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> {
                    log.error("User with email {} could not be found", email); // Error log
                    return new UsernameNotFoundException("User with email " + email + " could not be found");
                });
    }
}
