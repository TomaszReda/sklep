package pl.tomek.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {


    @GetMapping("/loginform")
    public String loginForm() {
        return "login_form";
    }
}
