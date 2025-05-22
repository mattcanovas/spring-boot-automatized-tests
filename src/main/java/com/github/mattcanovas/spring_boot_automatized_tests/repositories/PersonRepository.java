package com.github.mattcanovas.spring_boot_automatized_tests.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.mattcanovas.spring_boot_automatized_tests.entities.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByEmail(String email);

    @Query("SELECT p from Person p where p.firstName = ?1 and p.lastName = ?2")
    Optional<Person> findByFirstNameAndLastNameWithJPQLIndexesParameters(String firstName, String lastName);

    @Query("SELECT p from Person p where p.firstName = :firstName and p.lastName = :lastName")
    Optional<Person> findByFirstNameAndLastNameWithJPQLNamedParameters(
        @Param("firstName") String firstName, 
        @Param("lastName") String lastName);

    @NativeQuery("SELECT * FROM public.tb_person p WHERE p.first_name = ?1 AND p.last_name = ?2")
    Optional<Person> findByFirstNameAndLastNameWithNativeQueryIndexesParameters(String firstName, String lastName);

    @NativeQuery("SELECT * FROM public.tb_person p WHERE p.first_name = :firstName AND p.last_name = :lastName")
    Optional<Person> findByFirstNameAndLastNameWithNativeQueryNamedParameters(String firstName, String lastName);
}
