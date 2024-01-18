package org.acme.uispringboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    @Autowired
    private Environment env;

    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute( "apiBaseUrl", env.getProperty("api.base.url"));
        return "home.html";
    }
}
