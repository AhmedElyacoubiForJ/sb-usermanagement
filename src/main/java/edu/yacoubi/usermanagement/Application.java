package edu.yacoubi.usermanagement;

import edu.yacoubi.usermanagement.model.Role;
import edu.yacoubi.usermanagement.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Optional;

@SpringBootApplication
@EnableAsync
@RequiredArgsConstructor
public class Application {
	private final RoleRepository roleRepository;

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

