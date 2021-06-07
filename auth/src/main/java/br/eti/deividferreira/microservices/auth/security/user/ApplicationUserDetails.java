package br.eti.deividferreira.microservices.auth.security.user;

import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.eti.deividferreira.microservices.core.model.ApplicationUser;

public final class ApplicationUserDetails extends ApplicationUser implements UserDetails {

  public ApplicationUserDetails(@NotNull ApplicationUser applicationUser) {
    super(applicationUser);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return commaSeparatedStringToAuthorityList("ROLE_" + this.getRole());
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
  
}
