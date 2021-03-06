package br.eti.deividferreira.microservices.token.config;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;

import br.eti.deividferreira.microservices.core.property.JwtConfiguration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {
  protected final JwtConfiguration jwtConfiguration;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .cors().configurationSource(req -> new CorsConfiguration().applyPermitDefaultValues())
      .and()
        .sessionManagement().sessionCreationPolicy(STATELESS)
      .and()
        .exceptionHandling().authenticationEntryPoint((req, res, e) -> res.sendError(SC_UNAUTHORIZED))
      .and()
      .authorizeRequests()
        .antMatchers(jwtConfiguration.getLoginUrl(), "/**/swagger-ui.html").permitAll()
        .antMatchers(HttpMethod.GET, "/**/swagger-resources/**", "/**/webjars/springfox-swagger-ui/**", "/**/v2/api-docs/**").permitAll()
        .antMatchers("/course/v1/admin/**").hasRole("ADMIN")
        .antMatchers("/auth/user/**").hasAnyRole("ADMIN", "USER")
        .anyRequest().authenticated();
  }
  
}
