package edu.yacoubi.usermanagement.event.listener;

import edu.yacoubi.usermanagement.service.IEmailService;
import edu.yacoubi.usermanagement.event.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventListener {
    private final IEmailService emailService;

    @EventListener
    public void onUserEvent(UserEvent event) {

        log.info("Callback onUserEvent from UserEventListener with event: {}", event);

        switch (event.getType()) {
            case REGISTRATION -> {
                logInfo(event);
                emailService.sendNewAccountEmail(
                            event.getUser().getFirstName(),
                            event.getUser().getEmail(),
                            (String) event.getData().get("token")
                );
            }
            case RESET_PASSWORD -> {
                logInfo(event);
                emailService.sendPasswordResetEmail(
                    event.getUser().getFirstName(),
                    event.getUser().getEmail(),
                    (String) event.getData().get("token")
                );
            }
            default ->  {}
        }
    }

    private void logInfo(UserEvent event) {
        log.info("Event type {}", event.getType());
    }
}
