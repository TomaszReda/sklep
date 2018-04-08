package pl.tomek.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.tomek.model.Product;
import pl.tomek.repository.ProductRepository;
import pl.tomek.repository.UserRepository;

import java.awt.*;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> productList()
    {
        System.err.println(productRepository.findAll());
        return productRepository.findAll();
    }

    @GetMapping(path = "/my",produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<Product> myProduct()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        return productRepository.findByOwner(name);
    }

    @GetMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Product productList(@PathVariable Long id)
    {
        return productRepository.findOne(id);
    }


    @GetMapping(path = "/my/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Product meproductList(@PathVariable int id)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Set<Product> my=productRepository.findByOwner(name);
        Object product[]= my.toArray();
       return (Product)product[id-1];
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveProduct(@RequestBody Product product)
    {

        productRepository.save(product);
    }

    @PostMapping(path = "/my",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveProduct2(@RequestBody Product product)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        product.setOwner(name);
        productRepository.save(product);
    }

}
