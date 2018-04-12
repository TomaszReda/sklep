package pl.tomek.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.tomek.model.Product;
import pl.tomek.model.Zdjecia;
import pl.tomek.repository.ProductRepository;
import pl.tomek.repository.ZdjeciaRepositoru;

import javax.persistence.Id;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Controller
public class MyController {
    private ProductRepository productRepository;
    private ZdjeciaRepositoru zdjeciaRepositoru;

    @Autowired
    public void setZdjeciaRepositoru(ZdjeciaRepositoru zdjeciaRepositoru) {
        this.zdjeciaRepositoru = zdjeciaRepositoru;
    }

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @GetMapping("/my")
    public String my(Model model, @RequestParam(defaultValue = "0") int page) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        Collection<? extends GrantedAuthority> au= auth.getAuthorities();
        GrantedAuthority grantedAuthority=new SimpleGrantedAuthority("ADMIN ROLE");
        boolean isAdmin=au.contains(grantedAuthority);

        model.addAttribute("isAdmin",isAdmin);
        model.addAttribute("username",name);
        model.addAttribute("nieznajomy","anonymousUser");


        Page<Product> all = productRepository.findByOwner(name, new PageRequest(page, 10));
        int ile = productRepository.findByOwner(name).size();

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

        return "myForm";
    }


    @GetMapping("/usun")
    public String usun(@RequestParam Long ID) {

        productRepository.delete(ID);
        return "redirect:my";
    }

    @GetMapping("/details")
    public String detail(@RequestParam Long ID, Model model) {


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

        return "myDetails";
    }

    @GetMapping("/edytuj")
    public String edytuj(@RequestParam Long ID, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        Collection<? extends GrantedAuthority> au= auth.getAuthorities();
        GrantedAuthority grantedAuthority=new SimpleGrantedAuthority("ADMIN ROLE");
        boolean isAdmin=au.contains(grantedAuthority);

        model.addAttribute("isAdmin",isAdmin);
        model.addAttribute("username",name);
        model.addAttribute("nieznajomy","anonymousUser");

        Product product = productRepository.findOne(ID);
        model.addAttribute("ID", ID);
        model.addAttribute("product", product);
        List<Zdjecia> zdjecia = product.getZdjecia();
        Set<String> zd = new HashSet<>();
        for (Zdjecia z : zdjecia) {
            zd.add(z.getAdres());
        }
        model.addAttribute("zdjecia", zd);
        return "EditDetailsForm";
    }


    @PostMapping("/edytuj")
    public String edytuj(@RequestParam Long ID, Model model, @Valid @ModelAttribute Product product, BindingResult bindingResult, @RequestParam(value = "plik[]",required = false) MultipartFile[] file) {
        System.err.println("cccc1");
        int size;
        if(file==null) {
            size =0;
        }
        else {
            size=file.length;

        if (file[0] != null) {
            if (file.length > 9) {
                model.addAttribute("limit", "Limit zdjec to 9");
                return "redirect:edytuj?ID=" + ID;
            }
            for (int i = 0; i < file.length; i++) {
                String images = file[i].getContentType();

                images = images.substring(0, images.indexOf('/'));

                if (images.equals("image")) {
                } else if (images.equals("application")) {
                    size--;
                } else {
                    model.addAttribute("badExtend", "Moga byc tylko zdjecia");
                    return "redirect:edytuj?ID=" + ID;
                }
            }}
        }

        System.err.println("cccc2");

        if (size >= 1) {
            product.getZdjecia().clear();
        }
        System.err.println("cccc3");
        if (size >= 1 && !bindingResult.hasErrors()) {
            for (int i = 0; i < file.length; i++) {
                try {
                    String extend = file[i].getOriginalFilename();
                    extend = extend.substring(extend.indexOf('.'));

                    UUID uuid = UUID.randomUUID();
                    String filename = "src\\main\\resources\\static\\images\\products\\" + uuid.toString() + extend;
                    byte[] bytes = file[i].getBytes();
                    File files = new File(filename);

                    files.createNewFile();
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(files));
                    bufferedOutputStream.write(bytes);
                    bufferedOutputStream.close();
                    Zdjecia zdjecia = new Zdjecia();
                    zdjecia.setAdres("images/products/" + uuid.toString() + extend);
                    zdjeciaRepositoru.save(zdjecia);
                    product.getZdjecia().add(zdjecia);


                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        System.err.println("cccc4");
        if (bindingResult.hasErrors()) {
            return "redirect:edytuj?ID=" + ID;
        } else {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName(); //get logged in username
            product.setOwner(name);
            
            productRepository.save(product);

        }

        return "redirect:details?ID=" + ID;
    }
}