package pl.tomek.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.tomek.model.Product;
import pl.tomek.model.User;
import pl.tomek.model.UserRole;
import pl.tomek.repository.ProductRepository;
import pl.tomek.repository.UserRepository;
import pl.tomek.repository.UserRoleRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller
public class AdminController {


    private ProductRepository productRepository;
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

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
       Collection<? extends GrantedAuthority> au= auth.getAuthorities();
       GrantedAuthority grantedAuthority=new SimpleGrantedAuthority("ADMIN ROLE");
       boolean isAdmin=au.contains(grantedAuthority);

       model.addAttribute("isAdmin",isAdmin);
       model.addAttribute("username",name);
       model.addAttribute("nieznajomy","anonymousUser");

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
       System.out.println(product);
        productRepository.delete(product);
       return "redirect:admin";
   }

   @PostMapping("/deleteUser")
    public String usun(@RequestParam String nazwa,Model model) {
       System.err.println("aaanazwa"+nazwa);
       User user = userRepository.findByLogin(nazwa);
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       UserRole userRole=userRoleRepository.findByRole("ADMIN ROLE");
       System.err.println("aaaa User"+user);
       System.err.println("aaaa UserRole"+userRole);
       System.err.println("aaaa "+user.getUserRoleSet()+" : "+userRole);
       System.err.println("aaaaaa"+user.getUserRoleSet().contains(userRole));
    if(!user.getUserRoleSet().contains(userRole)) {

        System.err.println("bbbbb");
        userRepository.delete(user);
        Set<Product> productSet = productRepository.findByOwner(user.getLogin());
        System.err.println("aaaa productSet"+productSet);
        for (Product p : productSet) {
            productRepository.delete(p);
        }
        return "redirect:admin";
    }
       return "redirect:admin";
   }
}
