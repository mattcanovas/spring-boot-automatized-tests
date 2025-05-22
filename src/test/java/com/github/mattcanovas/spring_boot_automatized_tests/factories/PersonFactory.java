package com.github.mattcanovas.spring_boot_automatized_tests.factories;

import com.github.mattcanovas.spring_boot_automatized_tests.entities.Person;
import com.github.mattcanovas.spring_boot_automatized_tests.utils.Constants;

public class PersonFactory {
    
    private PersonFactory() {}

    public static Person createPerson() {
        return new Person();
    }

    public static Person createDefaultPerson() {
        return new Person(Constants.PERSON_DEFAULT_FIRST_NAME, Constants.PERSON_DEFAULT_LAST_NAME, Constants.PERSON_DEFAULT_EMAIL);
    }

    public static Person createCustomPerson(String firstName, String lastName, String email) {
        return new Person(firstName, lastName, email);
    }

}
