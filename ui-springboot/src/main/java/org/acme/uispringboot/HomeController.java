package org.acme.uispringboot;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    @Autowired
    private Environment env;

    @RequestMapping("/")
    public String home(@AuthenticationPrincipal Saml2AuthenticatedPrincipal principal, Model model){
        model.addAttribute("name", principal.getName());
        String email = principal.getFirstAttribute("email");
        // if email is not preset, try Auth0 attribute name
        if (email == null) {
            email = principal.getFirstAttribute("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress");
        }
        model.addAttribute("emailAddress", email);
        model.addAttribute("userAttributes", principal.getAttributes());

        model.addAttribute( "apiBaseUrl", env.getProperty("api.base.url"));
        return "home.html";
    }
}
