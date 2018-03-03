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

@Controller
public class SearchController {

    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/search")
    public String saerch(Model model,@RequestParam(defaultValue = "0") int page)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        model.addAttribute("username",name);
        model.addAttribute("nieznajomy","anonymousUser");
        Page<Product> all=productRepository.findAll(new PageRequest(page,5));
        int ile=productRepository.findAll().size();

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

        }
        model.addAttribute("ile",tab);
        model.addAttribute("products", all);


        return "searchForm";
    }
}
