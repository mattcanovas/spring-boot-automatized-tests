package com.github.mattcanovas.spring_boot_automatized_tests.integration.suite;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.github.mattcanovas.spring_boot_automatized_tests.integration.repository.PersonRepositoryIntegrationTest;
import com.github.mattcanovas.spring_boot_automatized_tests.integration.swagger.SwaggerIntegrationTest;

@Suite
@SelectClasses({ PersonRepositoryIntegrationTest.class, SwaggerIntegrationTest.class })
public class IntegrationSuite {

}
