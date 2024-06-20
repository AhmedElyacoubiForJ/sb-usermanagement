package edu.yacoubi.usermanagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        //MyOwnAuthenticationProvider authProvider = new MyOwnAuthenticationProvider(userDetailsService);
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // spring boot security filter chain Bean
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // configure spring security to handle authentication, authorization,
        // CSRF protection, and logout functionality for the application.
        return http.csrf(AbstractHttpConfigurer::disable)
                // this method is used to define access rules for different request patterns.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/error",
                                "/registration/**",
                                "/send-email-test",
                                // rules pattern for API endpoint
                                "/api/v1/user/register/**",
                                "/api/v1/user/verify/account",
                                "/api/v1/user/login",
                                "/api/v1/user/reset/password/request"
                        ).permitAll()
                        // Any other request requires authentication
                        .anyRequest()
                        .authenticated()
                )
                // is used to configure form-based login.
                .formLogin(form -> form
                        .loginPage("/login")        // Page
                        .usernameParameter("email") // username is set to email
                        .defaultSuccessUrl("/")     // after successful login, default success URL is set to "/"
                        .permitAll()                // form-based login are permitted to all.
                )
                // is used to configure logout functionality.
                .logout(logout -> logout
                        // invalid HTTP session
                        .invalidateHttpSession(true)
                        // clear Authentication object.
                        .clearAuthentication(true)
                        // logout request matcher is set to "/logout"
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        // after logout, URL is set to "/"
                        .logoutSuccessUrl("/")
                )
                .build();
    }
}