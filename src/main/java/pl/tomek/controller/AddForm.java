package pl.tomek.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.tomek.model.Product;
import pl.tomek.repository.ProductRepository;

import javax.validation.Valid;

@Controller
public class AddForm {

    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/add")
    public String add(Model model)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        model.addAttribute("username",name);
        model.addAttribute("nieznajomy","anonymousUser");
        model.addAttribute("product",new Product());
        return "addForm";
    }

    @PostMapping("/add")
    public String dodaj(@Valid @ModelAttribute Product product, BindingResult bindingResult,Model model)
    {
        if(bindingResult.hasErrors())
        {
            return "addForm";
        }
        else {


            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName(); //get logged in username
            product.setOwner(name);
            productRepository.save(product);
            model.addAttribute("add","dodano");
            model.addAttribute("username",name);
            model.addAttribute("nieznajomy","anonymousUser");
            return "addForm";
        } }
}
