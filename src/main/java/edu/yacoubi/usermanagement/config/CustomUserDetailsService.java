package edu.yacoubi.usermanagement.config;

import edu.yacoubi.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
// is responsible for loading user data from the database
// and providing it to the Spring Security Framework for
// authentication and authorization purposes.
// requirement by Spring Security is to implement UserDetailsService
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(
                                "User with email " + email + " could not be found"
                        )
                );
    }
}
