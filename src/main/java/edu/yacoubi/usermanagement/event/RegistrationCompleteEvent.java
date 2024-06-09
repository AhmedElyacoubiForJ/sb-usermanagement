package edu.yacoubi.usermanagement.event;

import edu.yacoubi.usermanagement.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@Deprecated
public class RegistrationCompleteEvent extends ApplicationEvent {
    private final User user;
    private final String confirmationUrl;

    public RegistrationCompleteEvent(User user, String confirmationUrl) {
        super(user);
        this.user = user;
        this.confirmationUrl = confirmationUrl;
    }
}
