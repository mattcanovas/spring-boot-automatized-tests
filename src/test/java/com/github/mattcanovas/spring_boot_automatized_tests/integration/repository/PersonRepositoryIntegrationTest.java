package com.github.mattcanovas.spring_boot_automatized_tests.integration.repository;

import static com.github.mattcanovas.spring_boot_automatized_tests.utils.Constants.PERSON_DEFAULT_FIRST_NAME;
import static com.github.mattcanovas.spring_boot_automatized_tests.utils.Constants.PERSON_DEFAULT_LAST_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.github.mattcanovas.spring_boot_automatized_tests.entities.Person;
import com.github.mattcanovas.spring_boot_automatized_tests.factories.PersonFactory;
import com.github.mattcanovas.spring_boot_automatized_tests.integration.AbstractIntegrationConfiguration;
import com.github.mattcanovas.spring_boot_automatized_tests.repositories.PersonRepository;
import com.github.mattcanovas.spring_boot_automatized_tests.utils.Constants;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonRepositoryIntegrationTest extends AbstractIntegrationConfiguration {
    private final PersonRepository repository;

    private Person person0_;

    @BeforeEach
    public void setup() {
        person0_ = PersonFactory.createDefaultPerson();
    }

    @Autowired
    public PersonRepositoryIntegrationTest(PersonRepository repository) {
        this.repository = repository;
    }

    @Test
    public void testGivenPersonObject_WhenSave_ShouldReturnSavedPerson() {
        this.repository.save(person0_);

        assertNotEquals(person0_, null);
        assertTrue(person0_.getId() > 0);
    }

    @Test
    public void testGivenPersonList_WhenFindAll_ThenReturnPersonList() {
        this.repository.saveAll(new ArrayList<Person>() {
            {
                add(person0_);
                add(new Person("Juliana", PERSON_DEFAULT_LAST_NAME, Constants.PERSON_DEFAULT_EMAIL));
            }
        });

        List<Person> persons = this.repository.findAll();

        assertNotNull(persons);
        assertEquals(2, persons.size());
    }

    @Test
    public void testGivenPersonObject_AfterSaved_WhenGetReferenceById_ShouldReturnSavedPersonBefore() {
        this.repository.save(this.person0_);

        Person personFinded = this.repository.getReferenceById(person0_.getId());

        assertNotNull(personFinded);
        assertEquals(personFinded.getId(), person0_.getId());
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
        this.repository.save(this.person0_);

        Person personSaved = this.repository.findById(person0_.getId()).get();
        personSaved.setFirstName("Rafael");
        personSaved.setEmail("validEmailForRafael@gmail.com");

        Person updatedPerson = this.repository.save(personSaved);

        assertNotNull(updatedPerson);
        assertEquals(person0_.getId(), updatedPerson.getId());
        assertEquals("Rafael", updatedPerson.getFirstName());
        assertEquals("validEmailForRafael@gmail.com", updatedPerson.getEmail());
    }

    @Test
    public void testGivenPersonObject_AfterSaved_WhenDeleted_ShoudlReturnNonePerson() {
        this.repository.save(this.person0_);

        this.repository.deleteById(person0_.getId());

        Optional<Person> personOptional = this.repository.findById(person0_.getId());

        assertTrue(personOptional.isEmpty());
    }

    @Test
    public void testGivenPersonObject_AfterSaved_WhenFindByFirstNameAndLastNameWithJPQLIndexParameters_ShouldReturnSavedPersonBefore() {
        this.repository.save(this.person0_);

        Person personFinded = this.repository
                .findByFirstNameAndLastNameWithJPQLIndexesParameters(person0_.getFirstName(), person0_.getLastName())
                .get();

        assertNotNull(personFinded);
        assertEquals(personFinded.getFirstName(), person0_.getFirstName());
        assertEquals(personFinded.getLastName(), person0_.getLastName());
    }

    @Test
    public void testGivenPersonObject_AfterSaved_WhenFindByFirstNameAndLastNameWithJPQLNamedParameters_ShouldReturnSavedPersonBefore() {
        this.repository.save(person0_);

        Person personFinded = this.repository
                .findByFirstNameAndLastNameWithJPQLNamedParameters(person0_.getFirstName(), person0_.getLastName())
                .get();

        assertNotNull(personFinded);
        assertEquals(personFinded.getFirstName(), person0_.getFirstName());
        assertEquals(personFinded.getLastName(), person0_.getLastName());
    }

    @Test
    public void testGivenPersonObject_AfterSaved_WhenFindByFirstNameAndLastNameWithNativeQueryIndexParameters_ShouldReturnSavedPersonBefore() {

        this.repository.save(this.person0_);

        Person personFinded = this.repository.findByFirstNameAndLastNameWithNativeQueryIndexesParameters(
                person0_.getFirstName(), person0_.getLastName()).get();

        assertNotNull(personFinded);
        assertEquals(personFinded.getFirstName(), person0_.getFirstName());
        assertEquals(personFinded.getLastName(), person0_.getLastName());
    }

    @Test
    public void testGivenPersonObject_AfterSaved_WhenFindByFirstNameAndLastNameWithNativeQueryNamedParameters_ShouldReturnSavedPersonBefore() {
        this.repository.save(person0_);

        Person personFinded = this.repository.findByFirstNameAndLastNameWithNativeQueryNamedParameters(
                person0_.getFirstName(), person0_.getLastName()).get();

        assertNotNull(personFinded);
        assertEquals(personFinded.getFirstName(), person0_.getFirstName());
        assertEquals(personFinded.getLastName(), person0_.getLastName());
    }
}
