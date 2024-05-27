package edu.yacoubi.usermanagement.api;

import edu.yacoubi.usermanagement.api.dto.EmailRequest;
import edu.yacoubi.usermanagement.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailResourceTest {
    private final EmailService emailService;

    @PostMapping("/send-email-test")
    public ResponseEntity<String> sendEmailTest(@RequestBody EmailRequest request) {
        emailService.sendSimpleEmail(
                request.getName(),
                request.getTo(),
                request.getSubject(),
                request.getMessage()
        );
        return  ResponseEntity.ok().body("Email sent successfully");
    }
}
