package edu.yacoubi.usermanagement.event.listener;

import edu.yacoubi.usermanagement.service.EmailService;
import edu.yacoubi.usermanagement.event.UserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {
    private final EmailService emailService;

    @EventListener
    public void onUserEvent(UserEvent event) {
        switch (event.getType()) {
            case REGISTRATION -> emailService
                    .sendNewAccountEmail(
                            event.getUser().getFirstName(),
                            event.getUser().getEmail(),
                            (String) event.getData().get("token")
                    );
            case RESETPASSWORD -> emailService.sendPasswordResetEmail(
                    event.getUser().getFirstName(),
                    event.getUser().getEmail(),
                    (String) event.getData().get("token")

            );
            default ->  {}
        }
    }
}
