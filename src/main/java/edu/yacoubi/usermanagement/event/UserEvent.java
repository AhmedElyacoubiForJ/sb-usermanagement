package edu.yacoubi.usermanagement.event;

import edu.yacoubi.usermanagement.enumeration.EventType;
import edu.yacoubi.usermanagement.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class UserEvent {
    private User user;
    private EventType type;
    private Map<?, ?> data;
}
