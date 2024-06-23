package edu.yacoubi.usermanagement.model;

import edu.yacoubi.usermanagement.utility.ConfirmationUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter @Setter @NoArgsConstructor
public class PasswordResetConfirmation {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String token;
    private Date expirationTime;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Boolean verified;

    public PasswordResetConfirmation(User user) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
        this.expirationTime = ConfirmationUtils.getExpirationTime();
        this.verified = false;
    }
}
