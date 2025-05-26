package com.github.mattcanovas.spring_boot_automatized_tests.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
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

        assertThat(persons, isNotNull());
        assertThat(persons.size(), is(2));
    }

    @Test
    public void testEmptyPersonsList_WhenFindAllPersons_ThenReturnEmptyPersonsList() {
        given(this.repository.findAll()).willReturn(Collections.emptyList());
        
        List<Person> persons = this.service.findAll();

        assertThat(persons.isEmpty(), is(true));
        assertThat(persons.size(), is(0));
    }

}
