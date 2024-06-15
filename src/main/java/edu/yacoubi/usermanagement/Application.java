package edu.yacoubi.usermanagement;

import edu.yacoubi.usermanagement.model.Role;
import edu.yacoubi.usermanagement.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Optional;

@SpringBootApplication
@EnableAsync
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
		return args -> {
			Optional<Role> userRole = roleRepository.findByNameIgnoreCase("ROLE_USER");
			if (!userRole.isPresent()) {
				roleRepository.save(new Role("ROLE_USER"));
			}
        };
	}
}

