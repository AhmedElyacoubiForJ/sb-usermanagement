package edu.yacoubi.usermanagement.confirmation;

import edu.yacoubi.usermanagement.user.User;
import edu.yacoubi.usermanagement.utility.TokenUtility;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Confirmation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expirationTime;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Confirmation(String token, User user) {
        this.token = token;
        this.user = user;
        this.expirationTime = TokenUtility.getExpirationTime();
    }

    public Confirmation(User user) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
        this.expirationTime = TokenUtility.getExpirationTime();
    }
}










