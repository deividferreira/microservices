package br.eti.deividferreira.microservices.gateway.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.eti.deividferreira.microservices.core.property.JwtConfiguration;
import br.eti.deividferreira.microservices.gateway.security.filter.GatewayJwtTokenAuthorizationFilter;
import br.eti.deividferreira.microservices.token.config.SecurityTokenConfig;
import br.eti.deividferreira.microservices.token.converter.TokenConverter;

@EnableWebSecurity
public class SecurityConfig  extends SecurityTokenConfig {

  private final JwtConfiguration jwtConfiguration;

  public SecurityConfig(JwtConfiguration jwtConfiguration) {
    super(jwtConfiguration);
    this.jwtConfiguration = jwtConfiguration;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .addFilterAfter(new GatewayJwtTokenAuthorizationFilter(jwtConfiguration, tokenConverter()), UsernamePasswordAuthenticationFilter.class);
    super.configure(http);
  }

  @Bean
  TokenConverter tokenConverter() {
    return new TokenConverter(jwtConfiguration);
  }
  
}
