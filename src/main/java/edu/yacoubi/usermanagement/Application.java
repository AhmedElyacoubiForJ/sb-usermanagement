package edu.yacoubi.usermanagement;

import edu.yacoubi.usermanagement.model.Role;
import edu.yacoubi.usermanagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
		return args -> {
            roleRepository.save(new Role("ROLE_USER"));
            roleRepository.save(new Role("ROLE_ADMIN"));
			roleRepository.save(new Role("ROLE_MANAGER"));
			roleRepository.save(new Role("ROLE_SUPERUSER"));
        };
	}
}

