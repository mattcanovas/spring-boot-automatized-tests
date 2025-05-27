package com.github.mattcanovas.spring_boot_automatized_tests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.github.mattcanovas", "com.github.mattcanovas.spring_boot_automatized_tests.configurations"})
public class SprintBootAutomatizedTestsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SprintBootAutomatizedTestsApplication.class, args);
	}

}
