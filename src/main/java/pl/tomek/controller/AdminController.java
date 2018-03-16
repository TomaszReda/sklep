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

import java.util.List;
import java.util.Set;

@Controller
public class AdminController {


    private ProductRepository productRepository;
    private UserRepository userRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/admin")
    public String admin(Model model)
   {
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String name = auth.getName(); //get logged in username
       model.addAttribute("username", name);
       model.addAttribute("nieznajomy", "anonymousUser");
    List<User> users=userRepository.findAll();
    model.addAttribute("users",users);
    List<Product> products=productRepository.findAll();
    model.addAttribute("products",products);

       return "adminForm";
   }


   @PostMapping("/deleteProduct")
   public String delete(@RequestParam String nazwa)
   {
        Product product=productRepository.findFirstByHeader(nazwa);
        productRepository.delete(product);
       return "redirect:admin";
   }

   @PostMapping("/deleteUser")
    public String usun(@RequestParam String nazwa,Model model)
   {

       if(!nazwa.equals("Admin")) {
           User user = userRepository.findByLogin(nazwa);
           userRepository.delete(user);
           Set<Product> productSet = productRepository.findByOwner(user.getLogin());
           for (Product p : productSet) {
               productRepository.delete(p);
           }
           return "redirect:admin";
       }
       return "redirect:admin";
   }
}
