package br.eti.deividferreira.microservices.core.docs;

import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

public class BaseSwaggerConfig {

  private final String basePackage;

  public BaseSwaggerConfig(String basePackage) {
    this.basePackage = basePackage;
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage(basePackage))
            .build()
            .apiInfo(metaData());
  }

  private ApiInfo metaData() {
    return new ApiInfoBuilder()
            .title("Spring Boot microservices")
            .description("Demo for Spring Boot Microservices")
            .version("1.0")
            .contact(new Contact("Deivid Ferreira", "https://github.com/deividferreira", "deividpse@gmail.com"))
            .license("GPLv3")
            .licenseUrl("https://www.gnu.org/licenses/gpl-3.0")
            .build();
  }
  
}
