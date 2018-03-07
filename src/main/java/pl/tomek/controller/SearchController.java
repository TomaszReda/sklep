package pl.tomek.controller;

import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.tomek.model.Product;
import pl.tomek.repository.ProductRepository;

import java.util.*;

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

    
    @RequestMapping(path = "/search")
    public String precisionSearch(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String word,@RequestParam(defaultValue = "trafnosc") String nazwaa) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        model.addAttribute("username", name);
        model.addAttribute("nieznajomy", "anonymousUser");

        String word2=word.toUpperCase();
        List<String> list = Arrays.asList(word2.split(" "));




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

        if(nazwaa=="trafnosc") {

        }
        if(nazwaa.equals("cenar")) {
          searchProducts.sort(Comparator.comparing(Product::getPrcies));
        }
        if(nazwaa.equals("cenam")) {
            searchProducts.sort(Comparator.comparing(Product::getPrcies));
            Collections.reverse(searchProducts);

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

        model.addAttribute("ile", tab);
        model.addAttribute("products", all);

        model.addAttribute("word",word);

        return "searchForm";
    }


}
