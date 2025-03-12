package com.example.Promatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.Promatter", "com.example.board"})
@EntityScan(basePackages = {"com.example.board.domain"})
@EnableJpaRepositories(basePackages = {"com.example.board.repository"})
public class PromatterApplication {

	public static void main(String[] args) {
		SpringApplication.run(PromatterApplication.class, args);
	}

}
