package com.github.mattcanovas.spring_boot_automatized_tests.repositories;

import static com.github.mattcanovas.spring_boot_automatized_tests.utils.Constants.PERSON_DEFAULT_EMAIL;
import static com.github.mattcanovas.spring_boot_automatized_tests.utils.Constants.PERSON_DEFAULT_FIRST_NAME;
import static com.github.mattcanovas.spring_boot_automatized_tests.utils.Constants.PERSON_DEFAULT_LAST_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.github.mattcanovas.spring_boot_automatized_tests.entities.Person;

@DataJpaTest
public class PersonRepositoryTest {

    private final PersonRepository repository;

    @Autowired
    public PersonRepositoryTest(PersonRepository repository) {
        this.repository = repository;
    }

    @Test
    public void testGivenPersonObject_WhenSave_ShouldReturnSavedPerson() {
        Person person0 = new Person(PERSON_DEFAULT_FIRST_NAME, PERSON_DEFAULT_LAST_NAME, PERSON_DEFAULT_EMAIL);

        Person savedPerson = this.repository.save(person0);

        assertNotEquals(savedPerson, null);
        assertTrue(savedPerson.getId() > 0);
    }

    @Test
    public void testGivenPersonList_WhenFindAll_ThenReturnPersonList() {
        this.repository.saveAll(new ArrayList<Person>() {
            {
                add(new Person(PERSON_DEFAULT_FIRST_NAME, PERSON_DEFAULT_LAST_NAME, PERSON_DEFAULT_EMAIL));
                add(new Person("Juliana", PERSON_DEFAULT_LAST_NAME, PERSON_DEFAULT_EMAIL));
            }
        });

        List<Person> persons = this.repository.findAll();

        assertNotNull(persons);
        assertEquals(2, persons.size());
    }

    @Test
    public void testGivenPersonObject_AfterSaved_WhenGetReferenceById_ShouldReturnSavedPersonBefore() {
        Person person0 = new Person(PERSON_DEFAULT_FIRST_NAME, PERSON_DEFAULT_LAST_NAME, PERSON_DEFAULT_EMAIL);
        person0 = this.repository.save(person0);

        Person personFinded = this.repository.getReferenceById(person0.getId());

        assertNotNull(personFinded);
        assertEquals(personFinded.getId(), person0.getId());
    }
}
