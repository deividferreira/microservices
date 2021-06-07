package br.eti.deividferreira.microservices.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApplicationUser implements AbstractEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  @NotNull(message = "The field 'username' is mandatory")
  private String username;
  @ToString.Exclude
  @Column(nullable = false)
  @NotNull(message = "The field 'password' is mandatory")
  private String password;
  @Column(nullable = false)
  @NotNull(message = "The field 'role' is mandatory")
  private String role;

  public ApplicationUser() {
    this.role = "USER";
  }

  public ApplicationUser(@NotNull ApplicationUser applicationUser) {
    this.id = applicationUser.getId();
    this.username = applicationUser.getUsername();
    this.password = applicationUser.getPassword();
    this.role = applicationUser.getRole();
  }
  
}
