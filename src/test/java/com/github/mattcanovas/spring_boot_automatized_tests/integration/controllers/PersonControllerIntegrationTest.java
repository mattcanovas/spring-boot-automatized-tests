package com.github.mattcanovas.spring_boot_automatized_tests.integration.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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

@TestMethodOrder(OrderAnnotation.class)
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
        person = PersonFactory.createDefaultPerson();
    }

    @Test
    @Order(1)
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

    @Test
    @Order(2)
    public void integrationTestGivenPersonObject_When_UpdateOnePerson_ShouldReturnAPersonObject() throws Exception {
        person.setFirstName("Miguel");
        person.setLastName("O'hara");

        String response = RestAssured.given().spec(specification)
                .contentType(IntegrationEnvironmentConfiguration.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Person updatedPerson = mapper.readValue(response, Person.class);
        person = updatedPerson;

        assertNotNull(updatedPerson);

        assertNotNull(updatedPerson.getId());
        assertNotNull(updatedPerson.getFirstName());
        assertNotNull(updatedPerson.getLastName());
        assertNotNull(updatedPerson.getEmail());
        assertThat(updatedPerson.getId(), greaterThan(0L));
        assertThat(updatedPerson.getFirstName(), is(person.getFirstName()));
        assertThat(updatedPerson.getLastName(), is(person.getLastName()));
        assertThat(updatedPerson.getEmail(), is(person.getEmail()));
    }

    @Test
    @Order(3)
    public void integrationTestGivenPersonObject_When_FindById_ShouldReturnAPersonObject() throws Exception {
        String response = RestAssured.given().spec(specification)
            .pathParam("id", person.getId())
            .when()
            .get("{id}")
           .then()
           .statusCode(200)
           .extract()
           .body()
           .asString();

        Person foundedPerson = mapper.readValue(response, Person.class);

        assertNotNull(foundedPerson);
        
        assertNotNull(foundedPerson.getId());
        assertNotNull(foundedPerson.getFirstName());
        assertNotNull(foundedPerson.getLastName());
        assertNotNull(foundedPerson.getEmail());
        assertThat(foundedPerson.getId(), greaterThan(0L));
        assertThat(foundedPerson.getFirstName(), is(person.getFirstName()));
        assertThat(foundedPerson.getLastName(), is(person.getLastName()));
        assertThat(foundedPerson.getEmail(), is(person.getEmail()));
    }

    @Test
    @Order(4)
    public void integrationTestGivenPersonObject_AfterCreated_WhenFindAll_ShouldReturnAPersonList() throws Exception {
        Person anotherPerson = PersonFactory.createCustomPerson("Miles", "Morales", "spiderman@gmail.com");
        RestAssured.given().spec(specification)
           .contentType(IntegrationEnvironmentConfiguration.CONTENT_TYPE_JSON)
           .body(anotherPerson)
           .when()
           .post()
           .then()
           .statusCode(201);

        String response = RestAssured.given().spec(specification)
          .when()
          .get()
          .then()
          .statusCode(200)
          .extract()
          .body()
          .asString();

        List<Person> persons = Arrays.asList(mapper.readValue(response, Person[].class));

        Person firstPerson = persons.get(0);
        Person secondPerson = persons.get(1);

        assertNotNull(firstPerson.getId());
        assertNotNull(firstPerson.getFirstName());
        assertNotNull(firstPerson.getLastName());
        assertNotNull(firstPerson.getEmail());
        assertThat(firstPerson.getId(), greaterThan(0L));
        assertThat(firstPerson.getFirstName(), is(person.getFirstName()));
        assertThat(firstPerson.getLastName(), is(person.getLastName()));
        assertThat(firstPerson.getEmail(), is(person.getEmail()));

        assertNotNull(secondPerson.getId());
        assertNotNull(secondPerson.getFirstName());
        assertNotNull(secondPerson.getLastName());
        assertNotNull(secondPerson.getEmail());
        assertThat(secondPerson.getId(), greaterThan(0L));
        assertThat(secondPerson.getFirstName(), is(anotherPerson.getFirstName()));
        assertThat(secondPerson.getLastName(), is(anotherPerson.getLastName()));
        assertThat(secondPerson.getEmail(), is(anotherPerson.getEmail()));

    }

    @Test
    @Order(5)
    public void integrationTestGivenPersonObject_When_Delete_ShouldReturnNoContent() throws Exception {
        RestAssured.given().spec(specification)
          .pathParam("id", person.getId())
          .when()
          .delete("{id}")
          .then()
          .statusCode(204);
    }


}
