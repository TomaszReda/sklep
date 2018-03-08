package pl.tomek.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.tomek.model.Product;
import pl.tomek.model.Zdjecia;
import pl.tomek.repository.ProductRepository;

import javax.persistence.Id;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
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




            Page<Product> all=productRepository.findByOwner(name,new PageRequest(page,10));
            int ile=productRepository.findByOwner(name).size();

            if(ile%10==0)
            {
                ile=ile/10;
            }
            else
            {
             ile=ile/10+1;
            }
            int tab[]=new int[ile];
            for(int i=0;i<ile;i++)
            {
                tab[i]=i;

            }
            model.addAttribute("ile",tab);
            model.addAttribute("products", all);

        return "myForm";
    }





    @GetMapping("/usun")
    public String usun(@RequestParam Long ID)
    {
        productRepository.delete(ID);
        return "redirect:my";
    }

    @GetMapping("/details")
    public String detail(@RequestParam Long ID,Model model)
    {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        model.addAttribute("username",name);
        model.addAttribute("nieznajomy","anonymousUser");

        Product product=productRepository.findOne(ID);
        List<Zdjecia> zdjecia=product.getZdjecia();
        Set<String> zd=new HashSet<>();
        for(Zdjecia z:zdjecia)
        {
            zd.add(z.getAdres());
        }

        System.out.println(zd);
        model.addAttribute("zdjecia",zd);
        model.addAttribute("product",product);
        return "myDetails";
    }
    @GetMapping("/edytuj")
    public String edytuj(@RequestParam Long ID,Model model)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        model.addAttribute("username",name);
        model.addAttribute("nieznajomy","anonymousUser");
        Product product=productRepository.findOne(ID);
        model.addAttribute("ID",ID);
        model.addAttribute("product",product);
        return "EditDetailsForm";
    }



    @PostMapping("/edytuj")
    public String edytuj(@RequestParam Long ID, @Valid @ModelAttribute Product product, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()){
            return "edytuj?ID="+ID;
        }
        else
        {
        Product tmp=productRepository.findOne(ID);
        product.setLicytujacy(tmp.getLicytujacy());
        product.setOwner(tmp.getOwner());
        product.setID(product.getID()-1);

        productRepository.delete(ID);
        productRepository.save(product);
        ID=productRepository.save(product).getID();

        }

    return "redirect:details?ID="+ID;
    }









}
