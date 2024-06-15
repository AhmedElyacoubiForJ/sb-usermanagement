package edu.yacoubi.usermanagement.api;

import edu.yacoubi.usermanagement.api.dto.LoginRequest;
import edu.yacoubi.usermanagement.api.dto.Response;
import edu.yacoubi.usermanagement.api.dto.UserRequest;
import edu.yacoubi.usermanagement.service.ConfirmationService;
import edu.yacoubi.usermanagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import static edu.yacoubi.usermanagement.constants.TokenStatus.INVALID;
import static edu.yacoubi.usermanagement.constants.TokenStatus.EXPIRED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserResource {
    private final UserService userService;
    private final ConfirmationService confirmationService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid UserRequest uRequest,
                                                 HttpServletRequest request) {

        userService.createUser(uRequest.getFirstName(), uRequest.getLastName(),
                uRequest.getEmail(), uRequest.getPassword());

        return ResponseEntity
                .created(getUri())
                .body(getResponse(request, emptyMap(),
                        "Account created. Check your email to enable your account",
                        HttpStatus.CREATED)
                );
    }

    @GetMapping("/verify/account")
    public ResponseEntity<Response> verifyAccount(@RequestParam("token") String token,
                                                  HttpServletRequest request) {
        String verifiedAccountToken = userService.verifyAccountToken(token);

        switch (verifiedAccountToken) {
            case INVALID:
                return ResponseEntity
                        .created(getUri())
                        .body(getResponse(request, emptyMap(),
                                "Account not found",
                                HttpStatus.NOT_FOUND)
                        );
            case EXPIRED:
                return ResponseEntity
                        .created(getUri())
                        .body(getResponse(request, emptyMap(),
                                "Account expired. please try to register again",
                                HttpStatus.BAD_REQUEST)
                        );
            default: // VALID
                return ResponseEntity.created(getUri())
                        .body(getResponse(request, emptyMap(),
                                "Account verified. You can now login",
                                HttpStatus.ACCEPTED)
                        );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        UsernamePasswordAuthenticationToken unauthenticated;

        unauthenticated = UsernamePasswordAuthenticationToken
                .unauthenticated(loginRequest.getEmail(), loginRequest.getPassword());
        authentication = authenticationManager.authenticate(unauthenticated);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok().body(Map.of("user", authentication));
    }

    // 1. password reset request, scenario the user must first send email as username
    // 2. the System sent an email with token
    // 3. then the user must click on the link in the email to verify the account
    // 4. user send the new password to the system
    @PostMapping("/reset/password/request")
    public ResponseEntity<Response> resetPasswordRequest(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {

        userService.requestResetPasswordForUser(loginRequest.getEmail());

        return ResponseEntity.ok().build();
    }


    private URI getUri() {
        return URI.create("");
    }

    // TODO MOVING IT TO  MAKE A GLOBAL USE FOR IT
    public static Response getResponse(HttpServletRequest request, Map<?, ?> data, String message,
                                       HttpStatus status) {
        return new Response(LocalDateTime.now().toString(), status.value(), request.getRequestURI(),
                HttpStatus.valueOf(status.value()), message, EMPTY, data);
    }
}
