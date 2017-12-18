package com.keyfox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@ServletComponentScan
@SpringBootApplication
@ImportResource({"classpath:/spring/*.xml"})
@ComponentScan(basePackages = "com.keyfox.*")
public class KeyfoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeyfoxApplication.class, args);
	}
}
