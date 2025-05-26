package com.github.mattcanovas.spring_boot_automatized_tests.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.mattcanovas.spring_boot_automatized_tests.entities.Person;
import com.github.mattcanovas.spring_boot_automatized_tests.factories.PersonFactory;
import com.github.mattcanovas.spring_boot_automatized_tests.repositories.PersonRepository;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository repository;

    @InjectMocks
    private PersonService service;

    private Person person0_;

    private static final Integer ONE_INVOCATION = 1;

    @BeforeEach
    public void setup() {
        person0_ = PersonFactory.createDefaultPerson();
    }

    @Test
    public void testGivenPersonObject_WhenCreatePerson_ThenReturnCreatedPersonObject() {
        given(this.repository.findByEmail(anyString())).willReturn(Optional.empty());
        given(this.repository.save(person0_)).willReturn(person0_);

        Person createdPerson = this.service.create(person0_);

        assertNotNull(createdPerson);
        assertThat(createdPerson.getFirstName(), is(person0_.getFirstName()));
    }

    @Test
    public void testGivenPersonObjectThatWasAlreadySaved_WhenCreatePerson_ThenThrownIllegalStateException() {
        given(this.repository.findByEmail(anyString())).willReturn(Optional.of(person0_));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            this.service.create(person0_);
        });
        verify(repository, never()).save(any(Person.class));
        assertThat(exception.getClass(), is(IllegalStateException.class));
        assertThat(exception.getMessage(), is("Person already exist with given e-Mail: " + person0_.getEmail()));
    }

    @Test
    public void testGivenPersonsList_WhenFindAllPersons_ThenReturnPersonsList() {
        given(this.repository.findAll()).willReturn(List.of(person0_, PersonFactory.createCustomPerson("Juliana", "Canovas", "teste@gmail.com")));
        
        List<Person> persons = this.service.findAll();

        assertThat(persons.isEmpty(), is(false));
        assertThat(persons.size(), is(2));
    }

    @Test
    public void testEmptyPersonsList_WhenFindAllPersons_ThenReturnEmptyPersonsList() {
        given(this.repository.findAll()).willReturn(Collections.emptyList());
        
        List<Person> persons = this.service.findAll();

        assertThat(persons.isEmpty(), is(true));
        assertThat(persons.size(), is(0));
    }

    @Test
    public void testGivenPersonId_WhenFindPersonById_ThenReturnPersonObject() {
        given(this.repository.findById(person0_.getId())).willReturn(Optional.of(person0_));

        Person person = this.service.findById(person0_.getId());

        assertNotNull(person);
        assertThat(person.getFirstName(), is(person0_.getFirstName()));
    }

    @Test
    public void testGivenPersonIdThatDoesNotExist_WhenFindPersonById_ThenThrownIllegalStateException() {
        given(this.repository.findById(person0_.getId())).willReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            this.service.findById(person0_.getId());
        });
        assertThat(exception.getClass(), is(IllegalStateException.class));
        assertThat(exception.getMessage(), is("Person with given id: " + person0_.getId() + " does not exist!"));
    }

    @Test
    public void testGivenPersonObject_WhenUpdatePerson_ThenReturnUpdatedPersonObject() {
        given(this.repository.findById(person0_.getId())).willReturn(Optional.of(person0_));
        given(this.repository.save(person0_)).willReturn(person0_);

        Person updatedPerson = this.service.update(person0_);

        assertNotNull(updatedPerson);
        assertThat(updatedPerson.getFirstName(), is(person0_.getFirstName()));
    }

    @Test
    public void testGivenPersonObjectThatDoesNotExist_WhenUpdatePerson_ThenThrownIllegalStateException() {
        given(this.repository.findById(person0_.getId())).willReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            this.service.update(person0_);
        });
        assertThat(exception.getClass(), is(IllegalStateException.class));
        assertThat(exception.getMessage(), is("Person with given id: " + person0_.getId() + " does not exist!"));
    }

    @Test
    public void testGivenPersonId_WhenDeletePersonById_ThenReturnVoid() {
        given(this.repository.findById(person0_.getId())).willReturn(Optional.of(person0_));
        willDoNothing().given(this.repository).delete(person0_);

        this.service.delete(person0_.getId());

        verify(this.repository, times(ONE_INVOCATION)).delete(person0_);
    }

    @Test
    public void testGivenPersonIdThatDoesNotExist_WhenDeletePersonById_ThenThrownIllegalStateException() {
        given(this.repository.findById(person0_.getId())).willReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            this.service.delete(person0_.getId());
        });
        assertThat(exception.getClass(), is(IllegalStateException.class));
        assertThat(exception.getMessage(), is("Person with given id: " + person0_.getId() + " does not exist!"));
    }

}
