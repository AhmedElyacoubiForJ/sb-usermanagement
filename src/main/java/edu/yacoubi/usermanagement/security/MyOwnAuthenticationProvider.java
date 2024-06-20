package edu.yacoubi.usermanagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyOwnAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailsService customUserDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var userAuth = (UsernamePasswordAuthenticationToken) authentication;
        var userFromDb = customUserDetailsService.loadUserByUsername((String)userAuth.getPrincipal());

        var passwordFromRequest = (String) userAuth.getCredentials();
        if (passwordFromRequest.equals(userFromDb.getPassword())) {
                return UsernamePasswordAuthenticationToken.authenticated(userFromDb, "[PASSWORD PROTECTED]", userFromDb.getAuthorities());
            }
            throw new BadCredentialsException("Unable to authenticate");
        }

    @Override
    public boolean supports(Class<?> authentication) {
        //return true;
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
