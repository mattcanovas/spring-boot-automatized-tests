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
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.github.mattcanovas.spring_boot_automatized_tests.entities.Person;
import com.github.mattcanovas.spring_boot_automatized_tests.factories.PersonFactory;

@DataJpaTest
public class PersonRepositoryTest {

    private final PersonRepository repository;

    @Autowired
    public PersonRepositoryTest(PersonRepository repository) {
        this.repository = repository;
    }

    @Test
    public void testGivenPersonObject_WhenSave_ShouldReturnSavedPerson() {
        Person person0 = PersonFactory.createDefaultPerson();

        Person savedPerson = this.repository.save(person0);

        assertNotEquals(savedPerson, null);
        assertTrue(savedPerson.getId() > 0);
    }

    @Test
    public void testGivenPersonList_WhenFindAll_ThenReturnPersonList() {
        this.repository.saveAll(new ArrayList<Person>() {
            {
                add(PersonFactory.createDefaultPerson());
                add(new Person("Juliana", PERSON_DEFAULT_LAST_NAME, PERSON_DEFAULT_EMAIL));
            }
        });

        List<Person> persons = this.repository.findAll();

        assertNotNull(persons);
        assertEquals(2, persons.size());
    }

    @Test
    public void testGivenPersonObject_AfterSaved_WhenGetReferenceById_ShouldReturnSavedPersonBefore() {
        Person person0 = PersonFactory.createDefaultPerson();
        person0 = this.repository.save(person0);

        Person personFinded = this.repository.getReferenceById(person0.getId());

        assertNotNull(personFinded);
        assertEquals(personFinded.getId(), person0.getId());
    }

    @Test
    public void testGivenPersonObject_AfterSave_WhenFindByEmail_ShouldReturnSavedPersonBefore() {
        Person person0 = new Person(PERSON_DEFAULT_FIRST_NAME, PERSON_DEFAULT_LAST_NAME, "validemail@gmail.com");
        this.repository.save(person0);

        Person personFinded = this.repository.findByEmail(person0.getEmail()).get();

        assertNotNull(personFinded);
        assertEquals(person0.getEmail(), personFinded.getEmail());
    }

    @Test
    public void testGivenPersonObject_AfterUpdated_WhenFindById_ShoudlReturnUpdatedPersonBefore() {
        Person person0 = PersonFactory.createDefaultPerson();
        person0 = this.repository.save(person0);

        Person personSaved = this.repository.findById(person0.getId()).get();
        personSaved.setFirstName("Rafael");
        personSaved.setEmail("validEmailForRafael@gmail.com");

        Person updatedPerson = this.repository.save(personSaved);

        assertNotNull(updatedPerson);
        assertEquals(person0.getId(), updatedPerson.getId());
        assertEquals("Rafael", updatedPerson.getFirstName());
        assertEquals("validEmailForRafael@gmail.com", updatedPerson.getEmail());
    }

    @Test 
    public void testGivenPersonObject_AfterSaved_WhenDeleted_ShoudlReturnNonePerson() {
        Person person0 = PersonFactory.createDefaultPerson();
        person0 = this.repository.save(person0);

        this.repository.deleteById(person0.getId());

        Optional<Person> personOptional = this.repository.findById(person0.getId());

        assertTrue(personOptional.isEmpty());
    }

    @Test
    public void testGivenPersonObject_AfterSaved_WhenFindByFirstNameAndLastNameWithJPQLIndexParameters_ShouldReturnSavedPersonBefore() {
        Person person0 = PersonFactory.createDefaultPerson();
        person0 = this.repository.save(person0);

        Person personFinded = this.repository.findByFirstNameAndLastNameWithJPQLIndexesParameters(person0.getFirstName(), person0.getLastName()).get();

        assertNotNull(personFinded);
        assertEquals(personFinded.getFirstName(), person0.getFirstName());
        assertEquals(personFinded.getLastName(), person0.getLastName());
    }

    @Test
    public void testGivenPersonObject_AfterSaved_WhenFindByFirstNameAndLastNameWithJPQLNamedParameters_ShouldReturnSavedPersonBefore() {
        Person person0 = PersonFactory.createDefaultPerson();
        person0 = this.repository.save(person0);

        Person personFinded = this.repository.findByFirstNameAndLastNameWithJPQLNamedParameters(person0.getFirstName(), person0.getLastName()).get();

        assertNotNull(personFinded);
        assertEquals(personFinded.getFirstName(), person0.getFirstName());
        assertEquals(personFinded.getLastName(), person0.getLastName());
    }

    @Test
    public void testGivenPersonObject_AfterSaved_WhenFindByFirstNameAndLastNameWithNativeQueryIndexParameters_ShouldReturnSavedPersonBefore() {
        Person person0 = PersonFactory.createDefaultPerson();
        person0 = this.repository.save(person0);

        Person personFinded = this.repository.findByFirstNameAndLastNameWithNativeQueryIndexesParameters(person0.getFirstName(), person0.getLastName()).get();

        assertNotNull(personFinded);
        assertEquals(personFinded.getFirstName(), person0.getFirstName());
        assertEquals(personFinded.getLastName(), person0.getLastName());
    }

    @Test
    public void testGivenPersonObject_AfterSaved_WhenFindByFirstNameAndLastNameWithNativeQueryNamedParameters_ShouldReturnSavedPersonBefore() {
        Person person0 = PersonFactory.createDefaultPerson();
        person0 = this.repository.save(person0);

        Person personFinded = this.repository.findByFirstNameAndLastNameWithNativeQueryNamedParameters(person0.getFirstName(), person0.getLastName()).get();

        assertNotNull(personFinded);
        assertEquals(personFinded.getFirstName(), person0.getFirstName());
        assertEquals(personFinded.getLastName(), person0.getLastName());
    }
}
