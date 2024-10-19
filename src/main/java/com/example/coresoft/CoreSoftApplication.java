package com.example.coresoft;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class CoreSoftApplication {
	public static void main(String[] args) {
		SpringApplication.run(CoreSoftApplication.class, args);
	}
}
