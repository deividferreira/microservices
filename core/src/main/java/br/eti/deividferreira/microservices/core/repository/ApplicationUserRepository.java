package br.eti.deividferreira.microservices.core.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.eti.deividferreira.microservices.core.model.ApplicationUser;

@Repository
public interface ApplicationUserRepository extends PagingAndSortingRepository<ApplicationUser, Long> {
  
    Optional<ApplicationUser> findByUsername(String username);
}
