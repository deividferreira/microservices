package br.eti.deividferreira.microservices.token.filter;

import static br.eti.deividferreira.microservices.token.util.SecurityContextUtil.setSecurityContext;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nimbusds.jwt.SignedJWT;

import org.springframework.web.filter.OncePerRequestFilter;

import br.eti.deividferreira.microservices.core.property.JwtConfiguration;
import br.eti.deividferreira.microservices.token.converter.TokenConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class JwtTokenAuthorizationFilter extends OncePerRequestFilter {
  protected final JwtConfiguration jwtConfiguration;
  protected final TokenConverter tokenConverter;
  
  
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String header = request.getHeader(jwtConfiguration.getHeader().getName());

    if (!Objects.nonNull(header) || !header.startsWith(jwtConfiguration.getHeader().getPrefix())) {
      filterChain.doFilter(request, response);

      return;
    }

    String token = header.replace(jwtConfiguration.getHeader().getPrefix(), "").trim();
    setSecurityContext(equalsIgnoreCase("signed", jwtConfiguration.getType())
        ? validate(token) : decryptValidating(token));

    filterChain.doFilter(request, response);
  }

  @SneakyThrows
  private SignedJWT decryptValidating(String encryptedToken) {
    String signedToken = tokenConverter.decryptToken(encryptedToken);
    tokenConverter.valdiateTokenSignature(signedToken);
    return SignedJWT.parse(signedToken);
  }

  @SneakyThrows
  private SignedJWT validate(String signedToken) {
    tokenConverter.valdiateTokenSignature(signedToken);
    return SignedJWT.parse(signedToken);
  }
  
}
