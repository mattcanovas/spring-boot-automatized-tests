package com.github.mattcanovas.spring_boot_automatized_tests.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.mattcanovas.spring_boot_automatized_tests.entities.Person;
import com.github.mattcanovas.spring_boot_automatized_tests.services.PersonService;

@RestController
@RequestMapping("/v1/person")
public class PersonController {
    
    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Person>> findAll() {
        return new ResponseEntity<>(this.service.findAll(), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> create(@RequestBody Person person) {
        return new ResponseEntity<>(this.service.create(person), HttpStatus.CREATED);
    }

}
