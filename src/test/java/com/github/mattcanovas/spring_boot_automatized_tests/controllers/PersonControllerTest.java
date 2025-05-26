package com.github.mattcanovas.spring_boot_automatized_tests.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mattcanovas.spring_boot_automatized_tests.entities.Person;
import com.github.mattcanovas.spring_boot_automatized_tests.factories.PersonFactory;
import com.github.mattcanovas.spring_boot_automatized_tests.services.PersonService;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @MockitoBean
    private PersonService service;

    private final MockMvc mockMvc;

    private final ObjectMapper mapper;

    private Person person0_;

    @Autowired
    public PersonControllerTest(MockMvc mockMvc, ObjectMapper mapper) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;
    }

    @BeforeEach
    public void setup() {
        person0_ = PersonFactory.createDefaultPerson();
    }

    @Test
    public void testGivenPersonObject_WhenCreatePerson_ThenReturnSavedPerson()
            throws JsonProcessingException, Exception {
        // willAnswer is another way to return a value without using a specific object,
        // returning the same object that was passed as a parameter.
        given(this.service.create(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/v1/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(person0_)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(person0_.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person0_.getLastName())))
                .andExpect(jsonPath("$.email", is(person0_.getEmail())));
    }

    @Test
    public void testGivenRequestedGetPersons_WhenFindAllPersons_ThenReturnListOfPersons()
            throws JsonProcessingException, Exception {
        List<Person> persons = List.of(person0_, PersonFactory.createCustomPerson("John", "Doe", "EMAIL"));
        given(this.service.findAll()).willReturn(persons);

        ResultActions response = this.mockMvc.perform(get("/v1/person"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(persons.size())));
    }

}
