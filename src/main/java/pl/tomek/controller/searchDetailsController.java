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
import pl.tomek.model.Zdjecia;
import pl.tomek.repository.ProductRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class searchDetailsController {

    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/detailsSearch")
    public String details(Model model, @RequestParam Long ID) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        Collection<? extends GrantedAuthority> au= auth.getAuthorities();
        GrantedAuthority grantedAuthority=new SimpleGrantedAuthority("ADMIN ROLE");
        boolean isAdmin=au.contains(grantedAuthority);

        model.addAttribute("isAdmin",isAdmin);
        model.addAttribute("username",name);
        model.addAttribute("nieznajomy","anonymousUser");

        Product product = productRepository.findOne(ID);
        List<Zdjecia> zdjecia = product.getZdjecia();
        Set<String> zd = new HashSet<>();
        for (Zdjecia z : zdjecia) {
            zd.add(z.getAdres());
        }
        model.addAttribute("zdjecia", zd);
        model.addAttribute("product", product);
        return "searchDetails";

    }


    @GetMapping("/buy")
    public String buy(Model model, @RequestParam Long ID) {


        productRepository.delete(ID);


        return "redirect:succesBought";
    }

    @GetMapping("/succesBought")
    public String succes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        Collection<? extends GrantedAuthority> au= auth.getAuthorities();
        GrantedAuthority grantedAuthority=new SimpleGrantedAuthority("ADMIN ROLE");
        boolean isAdmin=au.contains(grantedAuthority);

        model.addAttribute("isAdmin",isAdmin);
        model.addAttribute("username",name);
        model.addAttribute("nieznajomy","anonymousUser");
        return "succesBoughtForm";
    }

    @PostMapping("/oferta")
    public String oferta(Model model, @RequestParam Long ID, @RequestParam double cena) {


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        Collection<? extends GrantedAuthority> au= auth.getAuthorities();
        GrantedAuthority grantedAuthority=new SimpleGrantedAuthority("ADMIN ROLE");
        boolean isAdmin=au.contains(grantedAuthority);

        model.addAttribute("isAdmin",isAdmin);
        model.addAttribute("username",name);
        model.addAttribute("nieznajomy","anonymousUser");


        Product product = productRepository.findOne(ID);

        double prices = product.getPrcies();
        System.err.println("PRICES "+prices);

        if (cena < prices * 1.05) {

            return "redirect:/detailsSearch?ID=" + ID;
        } else {
            product.setPrcies(cena);
            product.setLicytujacy(name);
            productRepository.save(product);
            return "redirect:/detailsSearch?ID=" + ID;
        }
    }

}
