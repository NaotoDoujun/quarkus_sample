package org.acme.uispringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@Controller
@SpringBootApplication
public class UiSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(UiSpringbootApplication.class, args);
	}

}
