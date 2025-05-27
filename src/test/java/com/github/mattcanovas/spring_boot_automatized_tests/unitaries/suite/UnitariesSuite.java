package com.github.mattcanovas.spring_boot_automatized_tests.unitaries.suite;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.github.mattcanovas.spring_boot_automatized_tests.unitaries.controllers.PersonControllerTest;
import com.github.mattcanovas.spring_boot_automatized_tests.unitaries.repositories.PersonRepositoryTest;
import com.github.mattcanovas.spring_boot_automatized_tests.unitaries.services.PersonServiceTest;

@Suite
@SelectClasses({ PersonControllerTest.class, PersonServiceTest.class, PersonRepositoryTest.class })
public class UnitariesSuite {

}
