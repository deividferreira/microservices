package br.eti.deividferreira.microservices.auth.security.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.eti.deividferreira.microservices.auth.security.filter.JwtUsernameAndPasswordAuthenticationFilter;
import br.eti.deividferreira.microservices.core.property.JwtConfiguration;
import br.eti.deividferreira.microservices.token.security.config.SecurityTokenConfig;
import br.eti.deividferreira.microservices.token.security.creator.TokenCreator;

@EnableWebSecurity
public class SecurityCredentialsConfig extends SecurityTokenConfig {

  private final UserDetailsService userDetailsService;
  private final TokenCreator tokenCreator;

  public SecurityCredentialsConfig(JwtConfiguration jwtConfiguration,
                                  @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
                                  TokenCreator tokenCreator) {
    super(jwtConfiguration);
    this.userDetailsService = userDetailsService;
    this.tokenCreator = tokenCreator;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfiguration, tokenCreator));
    super.configure(http);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService)
      .passwordEncoder(passwordEncoder());
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
}
