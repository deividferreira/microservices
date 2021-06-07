package br.eti.deividferreira.microservices.course.endpoint.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.eti.deividferreira.microservices.core.model.Course;
import br.eti.deividferreira.microservices.core.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

  private final CourseRepository courseRepository;

  public Iterable<Course> list(Pageable pageable) {
    log.info("Listing all courses");
    return courseRepository.findAll(pageable);
  }
  
}
