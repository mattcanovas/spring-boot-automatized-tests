package com.github.mattcanovas.spring_boot_automatized_tests.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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

    private Person person;

    @Autowired
    public PersonControllerTest(MockMvc mockMvc, ObjectMapper mapper) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;
    }

    @BeforeEach
    public void setup() {
        person = PersonFactory.createDefaultPerson();
    }

    @Test
    public void testGivenPersonObject_WhenCreatePerson_ThenReturnSavedPerson() throws Exception {
        // willAnswer is another way to return a value without using a specific object,
        // returning the same object that was passed as a parameter.
        given(service.create(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/v1/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(person)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }

    @Test
    public void testGivenRequestedGetPersons_WhenFindAllPersons_ThenReturnListOfPersons() throws Exception {
        List<Person> persons = List.of(person, PersonFactory.createCustomPerson("John", "Doe", "EMAIL"));
        given(service.findAll()).willReturn(persons);

        ResultActions response = mockMvc.perform(get("/v1/person"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(persons.size())));
    }

    @Test
    public void testGivenPersonId_WhenFindById_ThenReturnPersonObject() throws Exception {
        Long personId = 1L;
        given(service.findById(personId)).willAnswer((invocation) -> person);

        ResultActions response = mockMvc.perform(get("/v1/person/{id}", personId));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }

    @Test
    public void testGivenInvalidPersonId_WhenFindById_ThenReturnNotFound() throws Exception {
        Long personId = 1L;
        given(service.findById(personId)).willThrow(IllegalStateException.class);

        ResultActions response = mockMvc.perform(get("/v1/person/{id}", personId));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGivenPersonIdWithPersonObject_WhenUpdatePerson_ThenReturnUpdatedPersonObject() throws Exception {
        Long personId = 1L;
        person.setId(personId);
        given(service.findById(personId)).willAnswer((invocation) -> person);
        given(service.update(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/v1/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(person)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(person.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }

    @Test
    public void testGivenInvalidPersonIdWithPersonObject_WhenUpdatePerson_ThenReturnNotFound() throws Exception {
        Long personId = 1L;
        person.setId(personId);
        given(service.findById(personId)).willThrow(IllegalStateException.class);
        given(service.update(any(Person.class))).willAnswer(invocation -> invocation.getArgument(1));
        ResultActions response = mockMvc.perform(put("/v1/person")
               .contentType(MediaType.APPLICATION_JSON)
               .content(this.mapper.writeValueAsString(person)));

        response.andExpect(status().isNotFound())
               .andDo(print());
    }

}
