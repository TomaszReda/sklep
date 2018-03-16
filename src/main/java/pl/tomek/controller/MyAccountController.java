package pl.tomek.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.tomek.model.Product;
import pl.tomek.model.User;
import pl.tomek.repository.ProductRepository;
import pl.tomek.repository.UserRepository;

import java.util.Set;

@Controller
public class MyAccountController {
    private UserRepository userRepository;
    private ProductRepository productRepository;


    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("/myAccount")
    public String my(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        System.out.println(name);
        model.addAttribute("username", name);
        model.addAttribute("nieznajomy", "anonymousUser");
        User user = userRepository.findByLogin(name);
        model.addAttribute("user", user);
        return "myAccountForm";
    }





    @PostMapping("/zmienHaslo")
    public String zmien(@RequestParam String aktualne,@RequestParam String nowe,@RequestParam String nowe2,Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        model.addAttribute("username", name);
        model.addAttribute("nieznajomy", "anonymousUser");
        User user=userRepository.findByLogin(name);

        if(!nowe.equals(nowe2))
        {
            model.addAttribute("notEquals","Hasła muszą byc identyczne");
        }
        if(!user.getPassworld().equals(aktualne))
        {
            model.addAttribute("notEquals2","Hasło jest złe");
        }
        if(nowe.length()<9 || nowe.length()>15)
        {
            model.addAttribute("notLength1","Hasło powinno zawierac od 9 do 15 znaków");
        }
        if(nowe2.length()<9 || nowe2.length()>15)
        {
            model.addAttribute("notLength2","Hasło powinno zawierac od 9 do 15 znaków");
        }

        if(nowe.equals(nowe2) && user.getPassworld().equals(aktualne)&&!(nowe.length()<9 || nowe.length()>15)&&!(nowe2.length()<9 || nowe2.length()>15))
        {
            user.setPassworld(nowe);
            userRepository.save(user);

        }

        User aaa = userRepository.findByLogin(name);
        model.addAttribute("user", aaa);
        return "myAccountForm";
    }







    @PostMapping("/zmienEmail")
    public String haslo(Model model,@RequestParam String email,@RequestParam String aktualne){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        model.addAttribute("username", name);
        model.addAttribute("nieznajomy", "anonymousUser");
        User user=userRepository.findByLogin(name);
        if(!user.getPassworld().equals(aktualne))
            model.addAttribute("password","Złe hasło");
            User emaill=userRepository.findFirstByEmail(email);
        if(emaill!=null)
        {
            model.addAttribute("badEmail","Email juz w uzyciu jest");
        }
        if(user.getPassworld().equals(aktualne) && emaill==null)
        {
            user.setEmail(email);
            userRepository.save(user);
        }


        User aaa = userRepository.findByLogin(name);
        model.addAttribute("user", aaa);
        return "myAccountForm";
    }








       @PostMapping("/zmienLogin")
    public String zmien(@RequestParam String login, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        model.addAttribute("username", name);
        model.addAttribute("nieznajomy", "anonymousUser");

           if(name.equals("Admin"))
           {
               model.addAttribute("Admin","Nie mozna zmienic loginu Administratora");
               User aaa = userRepository.findByLogin(name);
               model.addAttribute("user", aaa);
               return "myAccountForm";
           }

        User users = userRepository.findByLogin(login);
        if (users != null) {
            model.addAttribute("badLogin", "Login jest juz zajety");
        }



        if (login.length() >= 5 && login.length() <= 12 && users == null && !name.equals("Admin")) {

            Set<Product> list = productRepository.findByOwner(name);
            for (Product p : list) {
                p.setOwner(login);
            }
            User user = userRepository.findByLogin(name);
            user.setLogin(login);
            userRepository.save(user);
            User aaa = userRepository.findByLogin(login);
            model.addAttribute("user", aaa);
            model.addAttribute("successs","Musisz sie teraz wylogować i zalogować ponowniej już z nowym loginem");

            return "myAccountForm";

        } else {
            model.addAttribute("logins", "Login powinien mieć od od 5 do 12 znaków");

        }
        User aaa = userRepository.findByLogin(name);
        model.addAttribute("user", aaa);
        return "myAccountForm";
    }


}
