package pl.tomek.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.tomek.model.User;

@Controller
public class HomeController {


    @GetMapping("/")
    public String home(Model model) {

        return "index";
    }

   @GetMapping("/testy")
    public String testy()
   {
       return "testy";
   }

}
