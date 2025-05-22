package com.github.mattcanovas.spring_boot_automatized_tests.repositories;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.github.mattcanovas.spring_boot_automatized_tests.entities.Person;

@DataJpaTest
public class PersonRepositoryTest {
    
    @Autowired
    private PersonRepository repository;

    @Test
    public void testGivenPersonObject_WhenSave_ShouldReturnSavedPerson() {
        Person person0 = new Person();
        person0.setFirstName("Matheus");
        person0.setLastName("Canovas");
        person0.setEmail("test@gmail.com");

        Person savedPerson = this.repository.save(person0);

        assertNotEquals(savedPerson, null);
        assertTrue(savedPerson.getId() > 0);
    }

}
