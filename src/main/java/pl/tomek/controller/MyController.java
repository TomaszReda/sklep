package pl.tomek.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.tomek.model.Product;
import pl.tomek.repository.ProductRepository;

import java.util.Set;

@Controller
public class MyController {
    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @GetMapping("/my")
    public String my(Model model,@RequestParam(defaultValue = "0") int page)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        model.addAttribute("username",name);
        model.addAttribute("nieznajomy","anonymousUser");




            Page<Product> all=productRepository.findByOwner(name,new PageRequest(page,5));
            int ile=productRepository.findByOwner(name).size();

            if(ile%5==0)
            {
                ile=ile/5;
            }
            else
            {
             ile=ile/5+1;
            }
            int tab[]=new int[ile];
            for(int i=0;i<ile;i++)
            {
                tab[i]=i;
                System.out.println(tab[i]);
            }
            model.addAttribute("ile",tab);
            model.addAttribute("products", all);

        return "myForm";
    }



    @GetMapping("/usun")
    public String usun(@RequestParam Long id)
    {
        productRepository.delete(id);
        return "redirect:my";
    }







}
