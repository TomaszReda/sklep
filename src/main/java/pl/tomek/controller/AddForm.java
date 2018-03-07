package pl.tomek.controller;

import org.hibernate.internal.CoreLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

@Controller
public class AddForm {

    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private ZdjeciaRepositoru zdjeciaRepositoru;

    @Autowired
    public void setZdjeciaRepositoru(ZdjeciaRepositoru zdjeciaRepositoru) {
        this.zdjeciaRepositoru = zdjeciaRepositoru;
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
    public String dodaj(@Valid @ModelAttribute Product product, BindingResult bindingResult,Model model,@RequestParam("plik[]") MultipartFile[] file)
    {

     if(file.length>0&& !bindingResult.hasErrors())
     {
         for(int i=0;i<file.length;i++) {
             String images = file[i].getContentType();
             images = images.substring(0, images.indexOf('/'));
             if (images.equals("image")) {
                 try {

                     String extend = file[i].getOriginalFilename();
                     extend = extend.substring(extend.indexOf('.'));
                     System.out.println();
                     UUID uuid = UUID.randomUUID();
                     String filename = "src\\main\\resources\\static\\images\\" + uuid.toString() + extend;
                     byte[] bytes = file[i].getBytes();
                     File files = new File(filename);
                     files.createNewFile();
                     BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(files));
                     bufferedOutputStream.write(bytes);
                     bufferedOutputStream.close();
                     Zdjecia zdjecia=new Zdjecia();
                     zdjecia.setAdres(filename);
                     product.getZdjecia().add(zdjecia);




                 } catch (IOException ex) {
                     ex.printStackTrace();
                 }
             } else {
                 model.addAttribute("badExtend", "To musi byc zdjecie");
                 return "addForm";
             }
         }


     }




        if(bindingResult.hasErrors())
        {
            return "addForm";
        }
        else {


            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName(); //get logged in username
            product.setOwner(name);
            productRepository.save(product);
            model.addAttribute("Nie","Nie");
            model.addAttribute("username",name);
            model.addAttribute("nieznajomy","anonymousUser");
            return "redirect:/succes";
        }
    }

        @GetMapping("/succes")
        public String succes(Model model)
        {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName(); //get logged in username
            model.addAttribute("username",name);
            model.addAttribute("nieznajomy","anonymousUser");
            return "succedAddForm";
        }
}
