package com.github.mattcanovas.spring_boot_automatized_tests.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.mattcanovas.spring_boot_automatized_tests.entities.Person;
import com.github.mattcanovas.spring_boot_automatized_tests.repositories.PersonRepository;

@Service
public class PersonService {
    
    private final PersonRepository repository;

    private final Logger logger = LoggerFactory.getLogger(PersonService.class);

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public Person create(Person person) {
        logger.info("Creating one person!");

        Optional<Person> savedPerson = this.repository.findByEmail(person.getEmail());

        if(savedPerson.isPresent()) {
            throw new IllegalStateException("Person already exist with given e-Mail: " + person.getEmail());
        }
        return this.repository.save(person);
    }

    public List<Person> findAll() {
        logger.info("Find all persons!");
        return this.repository.findAll();
    }

    public Person findById(Long id) {
        logger.info("Find one person by id: " + id);
        Optional<Person> person = this.repository.findById(id);
        if(person.isPresent()) {
            return person.get();
        }
        throw new IllegalStateException("Person with given id: " + id + " does not exist!");
    }

    public Person update(Person person0_) {
        logger.info("Updating one person!");

        Person entity = this.findById(person0_.getId());
        entity.setFirstName(person0_.getFirstName());
        entity.setLastName(person0_.getLastName());
        entity.setEmail(person0_.getEmail());

        return this.repository.save(entity);
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");
        Person entity = findById(id);
        this.repository.delete(entity);
    }

}
