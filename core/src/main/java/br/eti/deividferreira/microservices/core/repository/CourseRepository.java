package br.eti.deividferreira.microservices.core.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.eti.deividferreira.microservices.core.model.Course;

@Repository
public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {
  
}
