package com.github.mattcanovas.spring_boot_automatized_tests.integration.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.isNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mattcanovas.spring_boot_automatized_tests.configurations.IntegrationEnvironmentConfiguration;
import com.github.mattcanovas.spring_boot_automatized_tests.entities.Person;
import com.github.mattcanovas.spring_boot_automatized_tests.factories.PersonFactory;
import com.github.mattcanovas.spring_boot_automatized_tests.integration.AbstractIntegrationConfiguration;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PersonControllerIntegrationTest extends AbstractIntegrationConfiguration {

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static Person person;

    @BeforeAll
    public static void setup() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // Configuring the base specification of our ApplicationContext
        specification = new RequestSpecBuilder()
                .setBasePath("/v1/person")
                .setPort(IntegrationEnvironmentConfiguration.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @BeforeEach
    void setupBeforeEach() {
        person = PersonFactory.createDefaultPerson();
    }

    @Test
    public void integrationTestGivenPersonObject_When_CreateOnePerson_ShouldReturnAPersonObject() throws Exception {
        String response = RestAssured.given()
                .spec(specification)
                .contentType(IntegrationEnvironmentConfiguration.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .response()
                .asString();

        Person createdPerson = mapper.readValue(response, Person.class);
        person = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getEmail());
        assertThat(createdPerson.getId(), greaterThan(0L));
        assertThat(createdPerson.getFirstName(), is(person.getFirstName()));
        assertThat(createdPerson.getLastName(), is(person.getLastName()));
        assertThat(createdPerson.getEmail(), is(person.getEmail()));
    }

}
