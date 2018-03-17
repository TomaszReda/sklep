package pl.tomek.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.tomek.model.User;

@Controller
public class HomeController {


    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        System.out.println(name);
        System.out.println(auth.getDetails().toString());

        model.addAttribute("username",name);
        model.addAttribute("nieznajomy","anonymousUser");

        return "index";
    }



}
