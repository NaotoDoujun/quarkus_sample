package org.acme.uispringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@SpringBootApplication
public class UiSpringbootApplication {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "redirect:/home";
	}

	public static void main(String[] args) {
		SpringApplication.run(UiSpringbootApplication.class, args);
	}

}
