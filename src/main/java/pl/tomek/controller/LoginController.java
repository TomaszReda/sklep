package pl.tomek.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {


    @GetMapping("/loginform")
    public String loginForm() {
        return "login_form";
    }

    @RequestMapping("/formError")
    public String errors()
    {
        return "formError";
    }

}
