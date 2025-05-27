package com.github.mattcanovas.spring_boot_automatized_tests.integration.swagger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.mattcanovas.spring_boot_automatized_tests.configurations.IntegrationEnvironmentConfiguration;
import com.github.mattcanovas.spring_boot_automatized_tests.integration.AbstractIntegrationTest;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @Test
    public void testShouldDisplaySwaggerUIPage() {
        String content = RestAssured.given()
                .basePath("/swagger-ui/index.html")
                .port(IntegrationEnvironmentConfiguration.SERVER_PORT)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        assertThat(content.contains("Swagger UI"), is(true));
    }

}
