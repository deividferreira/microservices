package br.eti.deividferreira.microservices.auth.endpoint.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.eti.deividferreira.microservices.core.model.ApplicationUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("user")
@Api(value = "Endpoints to manage user's information")
public class UserInfoController {

  @GetMapping(value="info", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Will retrieve the information from user available in the token", response = ApplicationUser.class)
  public ResponseEntity<ApplicationUser> getUserInfo(Principal principal) {
    ApplicationUser applicationUser = (ApplicationUser) ( (UsernamePasswordAuthenticationToken) principal).getPrincipal();

    return new ResponseEntity<>(applicationUser, HttpStatus.OK);
  }
  
  
}
