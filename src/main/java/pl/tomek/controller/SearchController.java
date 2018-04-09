package pl.tomek.controller;

import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        Collection<? extends GrantedAuthority> au= auth.getAuthorities();
        GrantedAuthority grantedAuthority=new SimpleGrantedAuthority("ADMIN ROLE");
        boolean isAdmin=au.contains(grantedAuthority);

        model.addAttribute("isAdmin",isAdmin);
        model.addAttribute("username",name);
        model.addAttribute("nieznajomy","anonymousUser");



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
                model.addAttribute("option","Trafność: największa");
                model.addAttribute("option2","Wszystkie");
                model.addAttribute("option22","Wszystkie");
                model.addAttribute("option1","trafnosc");
                model.addAttribute("option33","Obojetnie");
                model.addAttribute("option3","Obojetnie");


        return "searchForm";
    }

    
    @GetMapping("/searchh")
    public String precisionSearch(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String word,@RequestParam(defaultValue = "trafnosc") String filtr,@RequestParam(defaultValue = "Wszystkie")String kategoria,
    @RequestParam(defaultValue = "Obojetnie") String stan
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        Collection<? extends GrantedAuthority> au= auth.getAuthorities();
        GrantedAuthority grantedAuthority=new SimpleGrantedAuthority("ADMIN ROLE");
        boolean isAdmin=au.contains(grantedAuthority);

        model.addAttribute("isAdmin",isAdmin);
        model.addAttribute("username",name);
        model.addAttribute("nieznajomy","anonymousUser");

        String word2=word.toUpperCase();

        List<String> list = Arrays.asList(word2.split(" "));
        List<Product> allProducts = new ArrayList<>();


        if(kategoria.equals("Wszystkie") && stan.equals("Obojetnie"))
        {
           allProducts = productRepository.findAll();


        }
        else if(kategoria.equals("Wszystkie") && !stan.equals("Obojetnie"))
        {
            allProducts=productRepository.findAllByState(stan);

        }
        else if(!kategoria.equals("Wszystkie") && stan.equals("Obojetnie"))
        {
            allProducts=productRepository.findAllByKategoria(kategoria);
        }
        else
        {
            allProducts=productRepository.findAllByStateAndKategoria(stan,kategoria);


        }


        List<Product> searchProducts = new ArrayList<>();
        if(!word.equals("")) {
            for (Product p : allProducts) {
                String header = p.getHeader();
                header = header.toUpperCase();
                List<String> headerList = Arrays.asList(header.split(" "));
                for (String s : headerList) {
                    if (list.contains(s)) {
                        searchProducts.add(p);
                        break;
                    }
                }
            }
        }
        else {
            searchProducts=allProducts;
        }

        if(filtr.equals("cenar")) {
            model.addAttribute("option","Cena: Rosonąco");
          searchProducts.sort(Comparator.comparing(Product::getPrcies));
        }
        else if(filtr.equals("cenam")) {
            model.addAttribute("option","Cena: Malejąco");
            searchProducts.sort(Comparator.comparing(Product::getPrcies));
            Collections.reverse(searchProducts);

        }
        else
        {
            model.addAttribute("option","Trafność: największa");

        }



        Page<Product> alls = new PageImpl<Product>(searchProducts,new PageRequest(page,10),searchProducts.size());
        // Nie działa wyswietla wszystkie produkt
        List<Product> all=new ArrayList<>();
        int n;
        int ilee=0;
        if(searchProducts.isEmpty())
        {
            ilee=0;
        }
        else {
            if(page==0)
            {
                if(searchProducts.size()<10)
                {
                    ilee=searchProducts.size()%10;
                }
                else
                {
                    ilee=10;
                }
            }
            else
            {
                if(searchProducts.size()<(page+1)*10)
                {
                    ilee=searchProducts.size()%10+page*10;
                }
                else
                {
                    ilee=(page+1)*10;
                }

            }
        }

        if(page==0)
        {
            n=0;
        }
        else {
            n=page*10;
        }
        for(int i=n;i<ilee;i++)
        {
            all.add(searchProducts.get(i));
        }



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


        model.addAttribute("option3",stan);
        model.addAttribute("option33",stan);
        model.addAttribute("option22",kategoria);
        model.addAttribute("option1",filtr);
        model.addAttribute("option2",kategoria);
        model.addAttribute("ile", tab);
        model.addAttribute("products", all);
        model.addAttribute("nazwa",filtr);
        model.addAttribute("word",word);

        return "searchForm";
    }


}
