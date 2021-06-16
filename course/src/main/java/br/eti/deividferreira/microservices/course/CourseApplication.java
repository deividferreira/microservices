package br.eti.deividferreira.microservices.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import br.eti.deividferreira.microservices.core.property.JwtConfiguration;

@EnableEurekaClient
@SpringBootApplication
@ComponentScan({"br.eti.deividferreira.microservices"})
@EnableConfigurationProperties(value = JwtConfiguration.class)
@EntityScan({"br.eti.deividferreira.microservices.core.model"})
@EnableJpaRepositories({"br.eti.deividferreira.microservices.core.repository"})
public class CourseApplication {

	public static void main(String... args) {
		SpringApplication.run(CourseApplication.class, args);
	}

}
