package edu.yacoubi.usermanagement.resource;

import edu.yacoubi.usermanagement.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserResource {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid UserRequest userRequest, HttpServletRequest request) {
        userService.createUser(userRequest.getFirstName(), userRequest.getLastName(), userRequest.getEmail(), userRequest.getPassword());

        return ResponseEntity.created(getUri()).body(getResponse(request, emptyMap(), "Account created. Check your email to enable your account", CREATED));
    }

    private URI getUri() {
        return URI.create("");
    }

    // TODO MOVING IT TO  MAKE A GLOBAL USE FOR IT
    public static Response getResponse(HttpServletRequest request, Map<?,?> data, String message, HttpStatus status) {
        return new Response(
                LocalDateTime.now().toString(),
                status.value(),
                request.getRequestURI(),
                HttpStatus.valueOf(status.value()),
                message,
                EMPTY,
                data
                );
    }
}
