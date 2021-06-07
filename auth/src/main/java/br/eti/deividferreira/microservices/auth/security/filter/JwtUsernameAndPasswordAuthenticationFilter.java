package br.eti.deividferreira.microservices.auth.security.filter;

import static java.util.Collections.emptyList;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.eti.deividferreira.microservices.core.model.ApplicationUser;
import br.eti.deividferreira.microservices.core.property.JwtConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;
  private final JwtConfiguration jwtConfiguration;

  @Override
  @SneakyThrows
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
    log.info("Attempting authentication...");
    ApplicationUser applicationUser = new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);

    if (!Objects.nonNull(applicationUser))
      throw new UsernameNotFoundException("Unable to retrieve the username or password");

    log.info("Creating the authentication object for the user '{}' and calling UserDetailServiceImpl loadUserByUsername", applicationUser);

    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
    usernamePasswordAuthenticationToken.setDetails(applicationUser);

    return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
  }

  @Override
  @SneakyThrows
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException, ServletException {
    log.info("Authentication was succesful for the user '{}', generating JWE token", authResult.getName());

    SignedJWT signedJWT = createSignedJWT(authResult);
    String encryptToken = encryptToken(signedJWT);

    log.info("Token generated successfully, adding it to the response header");

    response.addHeader("Access-Control-Expose-Header", "XSRF-TOKEN, "+ jwtConfiguration.getHeader().getName());
    response.addHeader(jwtConfiguration.getHeader().getName(), jwtConfiguration.getHeader().getPrefix() + encryptToken);
  }

  @SneakyThrows
  private SignedJWT createSignedJWT(Authentication auth) {
    log.info("Starting to create the signed JWT");

    ApplicationUser applicationUser = (ApplicationUser) auth.getPrincipal();
    JWTClaimsSet jwtClaimsSet = createJwtClaimsSet(auth, applicationUser);

    KeyPair keyPair = generateKeyPair();
    log.info("Building JWK from the RSA Keys");

    JWK jwk = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
      .keyID(UUID.randomUUID().toString())
      .build();
    SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
      .jwk(jwk)
      .type(JOSEObjectType.JWT)
      .build(), jwtClaimsSet);

    log.info("Signing the token with the private RSA Key");
    RSASSASigner signer = new RSASSASigner(keyPair.getPrivate());
    signedJWT.sign(signer);

    log.info("Serialized Token '{}'", signedJWT.serialize());

    return signedJWT;
  }

  private JWTClaimsSet createJwtClaimsSet(Authentication auth, ApplicationUser user) {
    log.info("Creating the JWTClaimsSet Object for '{}'", user);

    return new JWTClaimsSet.Builder()
      .subject(user.getUsername())
      .claim("authorities", auth.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList())
      ).issuer("https://github.com/deividferreira/microservices")
      .issueTime(new Date())
      .expirationTime(new Date(System.currentTimeMillis() + (jwtConfiguration.getExpiration() * 1000)))
      .build();
  }

  private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
    log.info("Generating RSA 2048 bits keys");
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

    generator.initialize(2048);

    return generator.genKeyPair();
  }

  private String encryptToken(SignedJWT signedJWT) throws JOSEException {
    log.info("Starting the encryptToken method");

    DirectEncrypter encrypter = new DirectEncrypter(jwtConfiguration.getPrivateKey().getBytes());

    JWEObject jweObject = new JWEObject(new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256)
      .contentType("JWT")
      .build(), new Payload(signedJWT));

    log.info("Encrypting token with system's private key");
    jweObject.encrypt(encrypter);
    log.info("Token encrypted");

    return jweObject.serialize();
  }
  
}
