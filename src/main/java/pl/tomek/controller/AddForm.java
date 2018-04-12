package pl.tomek.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

@Controller
public class AddForm {

    private ProductRepository productRepository;
    private ZdjeciaRepositoru zdjeciaRepositoru;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setZdjeciaRepositoru(ZdjeciaRepositoru zdjeciaRepositoru) {
        this.zdjeciaRepositoru = zdjeciaRepositoru;
    }

    @GetMapping("/add")
    public String add(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        Collection<? extends GrantedAuthority> au= auth.getAuthorities();
        GrantedAuthority grantedAuthority=new SimpleGrantedAuthority("ADMIN ROLE");
        boolean isAdmin=au.contains(grantedAuthority);

        model.addAttribute("isAdmin",isAdmin);
        model.addAttribute("username",name);
        model.addAttribute("nieznajomy","anonymousUser");

        model.addAttribute("product", new Product());
        return "addForm";
    }

    @PostMapping("/add")
    public String dodaj(@Valid @ModelAttribute Product product, BindingResult bindingResult, Model model, @RequestParam(value = "plik[]",required = false) MultipartFile[] file) {

        System.err.println("ccc"+file);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        model.addAttribute("username", name);
        model.addAttribute("nieznajomy", "anonymousUser");
        int size ;

        if(file==null) {
            size=0;
        }
        else {
        size=file.length;
            if(file[0] != null) {

                if (file.length > 9) {
                    model.addAttribute("limit", "Limit zdjec to 9");
                    return "addForm";
                }

                for (int i = 0; i < file.length; i++) {
                    String images = file[i].getContentType();

                    images = images.substring(0, images.indexOf('/'));

                    if (images.equals("image")) {
                    } else if (images.equals("application")) {
                        size--;
                    } else {
                        model.addAttribute("badExtend", "Moga byc tylko zdjecia");
                        return "addForm";
                    }
                }
            }




        }


        System.err.println("ccc2"+product);



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
                    product.getZdjecia().add(zdjecia);
                    System.out.println("");


                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }


        System.err.println("ccc3"+product);
        if (bindingResult.hasErrors()) {
            System.err.println("errors" +bindingResult.getAllErrors());
            return "addForm";
        } else {


            System.err.println("ccc4"+product);
            product.setOwner(name);
            System.err.println("ccc5"+product);
            productRepository.save(product);
            System.err.println("ccc6"+product);
            model.addAttribute("Nie", "Nie");
            System.err.println("ccc7"+product);
            return "redirect:/succes";
        }
    }

    @GetMapping("/succes")
    public String succes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        model.addAttribute("username", name);
        model.addAttribute("nieznajomy", "anonymousUser");
        return "succedAddForm";
    }
}