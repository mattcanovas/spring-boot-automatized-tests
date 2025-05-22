package com.github.mattcanovas.spring_boot_automatized_tests.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.mattcanovas.spring_boot_automatized_tests.entities.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    
}
