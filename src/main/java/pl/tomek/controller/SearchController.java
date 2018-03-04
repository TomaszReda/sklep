package pl.tomek.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.tomek.model.Product;
import pl.tomek.repository.ProductRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class SearchController {

    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/search")
    public String saerch(Model model, @RequestParam(defaultValue = "0") int page) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        model.addAttribute("username", name);
        model.addAttribute("nieznajomy", "anonymousUser");


        Page<Product> all = productRepository.findAll(new PageRequest(page, 10));
        int ile = productRepository.findAll().size();

        if (ile % 10 == 0) {
            ile = ile / 10;
        } else {
            ile = ile / 10 + 1;
        }
        int tab[] = new int[ile];
        for (int i = 0; i < ile; i++) {
            tab[i] = i;

        }
        model.addAttribute("ile", tab);
        model.addAttribute("products", all);


        return "searchForm";
    }


    @PostMapping("/search")
    public String precisionSearch(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam String word) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        model.addAttribute("username", name);
        model.addAttribute("nieznajomy", "anonymousUser");
        word=word.toUpperCase();
        List<String> list = Arrays.asList(word.split(" "));


        List<Product> allProducts = productRepository.findAll();
        List<Product> searchProducts = new ArrayList<>();
        for (Product p : allProducts) {
            String header = p.getHeader();
            header=header.toUpperCase();
            List<String> headerList = Arrays.asList(header.split(" "));
            for (String s : headerList) {
                if (list.contains(s)) {
                    searchProducts.add(p);
                    break;
                }
            }

        }
        Page<Product> all = new PageImpl<>(searchProducts, new PageRequest(page, 10), searchProducts.size());
        int ile = searchProducts.size();

        if (ile % 10 == 0) {
            ile = ile / 10;
        } else {
            ile = ile / 10 + 1;
        }
        int tab[] = new int[ile];
        for (int i = 0; i < ile; i++) {
            tab[i] = i;

        }
        System.out.println(ile);
        model.addAttribute("ile", tab);
        model.addAttribute("products", all);


        return "searchForm";
    }


}
