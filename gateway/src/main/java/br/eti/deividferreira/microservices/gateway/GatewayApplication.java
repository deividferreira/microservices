package br.eti.deividferreira.microservices.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import br.eti.deividferreira.microservices.core.property.JwtConfiguration;

@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
@EnableConfigurationProperties(value = JwtConfiguration.class)
@EntityScan({"br.eti.deividferreira.microservices.token.converter"})
public class GatewayApplication {

	public static void main(String... args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

}
