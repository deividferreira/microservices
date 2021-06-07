package br.eti.deividferreira.microservices.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import br.eti.deividferreira.microservices.core.property.JwtConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(value = JwtConfiguration.class)
@EntityScan({"br.eti.deividferreira.microservices.core.model"})
@EnableJpaRepositories({"br.eti.deividferreira.microservices.core.repository"})
public class AuthApplication {

	public static void main(String... args) {
		SpringApplication.run(AuthApplication.class, args);
	}

}
