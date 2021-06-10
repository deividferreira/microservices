package br.eti.deividferreira.microservices.gateway.security.filter;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.netflix.zuul.context.RequestContext;
import com.nimbusds.jwt.SignedJWT;

import br.eti.deividferreira.microservices.core.property.JwtConfiguration;
import br.eti.deividferreira.microservices.token.converter.TokenConverter;
import br.eti.deividferreira.microservices.token.filter.JwtTokenAuthorizationFilter;
import br.eti.deividferreira.microservices.token.util.SecurityContextUtil;
import lombok.SneakyThrows;

public class GatewayJwtTokenAuthorizationFilter extends JwtTokenAuthorizationFilter {

  public GatewayJwtTokenAuthorizationFilter(JwtConfiguration jwtConfiguration, TokenConverter tokenConverter) {
    super(jwtConfiguration, tokenConverter);
  }

  @Override
  @SneakyThrows
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String header = request.getHeader(jwtConfiguration.getHeader().getName());

    if (!Objects.nonNull(header) || !header.startsWith(jwtConfiguration.getHeader().getPrefix())) {
      filterChain.doFilter(request, response);

      return;
    }

    String token = header.replace(jwtConfiguration.getHeader().getPrefix(), "").trim();
    
    String signedToken = tokenConverter.decryptToken(token);
    tokenConverter.valdiateTokenSignature(signedToken);

    SecurityContextUtil.setSecurityContext(SignedJWT.parse(signedToken));

    String prefix = jwtConfiguration.getHeader().getPrefix();
    if (jwtConfiguration.getType().equalsIgnoreCase("signed"))
      RequestContext.getCurrentContext().addZuulRequestHeader(prefix, prefix + signedToken);

    filterChain.doFilter(request, response);
  }
  
}
