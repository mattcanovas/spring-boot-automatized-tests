package com.github.mattcanovas.spring_boot_automatized_tests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Optional;

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
        assertThat(exception.getClass(), is(IllegalStateException.class));
        assertThat(exception.getMessage(), is("Person already exist with given e-Mail: " + person0_.getEmail()));
    }

}
