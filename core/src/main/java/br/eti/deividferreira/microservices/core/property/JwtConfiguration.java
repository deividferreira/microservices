package br.eti.deividferreira.microservices.core.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "jwt.config")
public class JwtConfiguration {

  private String loginUrl;
  @NestedConfigurationProperty
  private Header header;
  private int expiration;
  private String privateKey;
  private String type;

  public JwtConfiguration() {
    this.loginUrl = "/login/**";
    this.expiration = 3600;
    this.privateKey = "cXvDhjVvZRG0Pjs3gUl3BURen44MipYj";
    this.type = "encrypted";
    this.header = new Header();
  }

  @Getter
  @Setter
  public static class Header {
    private String name;
    private String prefix;

    public Header() {
      this.name = "Authorization";
      this.prefix = "Bearer ";
    }
  }
  
}
