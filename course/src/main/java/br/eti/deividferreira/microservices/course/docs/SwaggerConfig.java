package br.eti.deividferreira.microservices.course.docs;

import org.springframework.context.annotation.Configuration;

import br.eti.deividferreira.microservices.core.docs.BaseSwaggerConfig;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

  public SwaggerConfig() {
    super("br.eti.deividferreira.microservices.course.endpoint.controller");
  }
  
}
