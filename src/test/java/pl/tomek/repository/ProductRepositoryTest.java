package pl.tomek.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.tomek.model.Product;

import pl.tomek.model.Zdjecia;

import java.util.ArrayList;


import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product product;


    @Before
    public void setUp() throws Exception {
        List<Zdjecia> list=new ArrayList<>();
        Zdjecia zdjecia=new Zdjecia();
        zdjecia.setAdres("adress");
        list.add(zdjecia);
        product=new Product(1L,"NAMEE","STATEE","HEADERR","KATEGORIAA",23,"OPISS","AUKCJAA","OWNERR","LICYTUJACYY",list);
        productRepository.save(product);
        Product product2=new Product(2L,"NAME","STATE","HEADER","KATEGORIA",23,"OPIS","AUKCJA","OWNER","LICYTUJACY",null);
        productRepository.save(product2);

    }



    @Test
    public void findByOwnerPage() {
        Assert.assertNotNull(productRepository.findByOwner(product.getOwner(),new PageRequest(0,10)));
    }

    @Test
    public void findByOwner() {
        Assert.assertNotNull(productRepository.findByOwner(product.getOwner()));

    }

    @Test
    public void findFirstByHeader() {
        Assert.assertNotNull(productRepository.findFirstByHeader(product.getHeader()));
    }

    @Test
    public void findAllByLicytujacy() {
        Assert.assertNotNull(productRepository.findAllByLicytujacy(product.getLicytujacy()));
    }

    @Test
    public void findAllByKategoria() {
        Assert.assertNotNull(productRepository.findAllByKategoria(product.getKategoria()));
    }

    @Test
    public void findAllByState() {
        Assert.assertNotNull(productRepository.findAllByState(product.getState()));
    }

    @Test
    public void findAllByStateAndKategoria() {
        Assert.assertNotNull(productRepository.findAllByKategoria(product.getKategoria()));
    }
}