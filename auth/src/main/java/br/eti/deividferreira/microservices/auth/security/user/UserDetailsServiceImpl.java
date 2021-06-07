package br.eti.deividferreira.microservices.auth.security.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.eti.deividferreira.microservices.core.model.ApplicationUser;
import br.eti.deividferreira.microservices.core.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final ApplicationUserRepository applicationUserRepository;
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("Searching in the BD the user by username: '{}'", username);

    ApplicationUser applicationUser = applicationUserRepository.findByUsername(username).orElseThrow(
      () -> new UsernameNotFoundException(String.format("Application user '%s' not found", username)));
      
    log.info("Application user found '{}'", applicationUser);

    return new ApplicationUserDetails(applicationUser);
  }


  
}
