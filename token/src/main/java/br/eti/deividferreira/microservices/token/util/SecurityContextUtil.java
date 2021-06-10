package br.eti.deividferreira.microservices.token.util;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import br.eti.deividferreira.microservices.core.model.ApplicationUser;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class SecurityContextUtil {

  private SecurityContextUtil() { }

  public static void setSecurityContext(SignedJWT signedJWT) {
    try {
      JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
      String username = claims.getSubject();
      if (!Objects.nonNull(username))
        throw new JOSEException("Username missing from JWT");

      List<String> authorities = claims.getStringListClaim("authorities");
      ApplicationUser user = ApplicationUser
        .builder()
        .id(claims.getLongClaim("userId"))
        .username(username)
        .role(String.join(",", authorities))
        .build();

      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, createAuthorities(authorities));
      auth.setDetails(signedJWT.serialize());

      SecurityContextHolder.getContext().setAuthentication(auth);
    } catch (Exception e) {
      log.error("Error setting security context");
      SecurityContextHolder.clearContext();
    }
  }

  private static List<SimpleGrantedAuthority> createAuthorities(List<String> authorities) {
    return authorities
      .stream()
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toList());
  }

}
