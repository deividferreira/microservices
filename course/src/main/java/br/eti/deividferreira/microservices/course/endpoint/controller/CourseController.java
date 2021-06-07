package br.eti.deividferreira.microservices.course.endpoint.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.eti.deividferreira.microservices.course.endpoint.service.CourseService;
import br.eti.deividferreira.microservices.core.model.Course;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/admin/courses")
public class CourseController {
  private final CourseService courseService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Iterable<Course>> list(Pageable pageable) {
    log.info("Request to Listing courses received");
    return new ResponseEntity<>(courseService.list(pageable), HttpStatus.OK);
  }
  
}