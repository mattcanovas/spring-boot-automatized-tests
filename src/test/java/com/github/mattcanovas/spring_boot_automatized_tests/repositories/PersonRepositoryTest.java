package com.github.mattcanovas.spring_boot_automatized_tests.repositories;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class PersonRepositoryTest {
    
    private final PersonRepository repository;

    public PersonRepositoryTest(PersonRepository repository) {
        this.repository = repository;
    }

}
