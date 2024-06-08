package edu.yacoubi.usermanagement.model;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;

    public Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
      return name.toString();
    }
}
