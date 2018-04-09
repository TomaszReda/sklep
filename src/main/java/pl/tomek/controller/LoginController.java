package pl.tomek.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.tomek.model.Product;
import pl.tomek.repository.ProductRepository;

import java.util.*;

@Controller
public class LoginController {


    @GetMapping("/loginform")
    public String loginForm(Model model) {


        return "login_form";
    }

    @GetMapping("/formError")
    public String errors()
    {
        return "formErrors";
    }


}
