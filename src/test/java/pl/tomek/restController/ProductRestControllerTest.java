package pl.tomek.restController;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.tomek.model.Product;
import pl.tomek.model.Zdjecia;
import pl.tomek.repository.ProductRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;


@RunWith(SpringJUnit4ClassRunner.class)
public class ProductRestControllerTest {

    @InjectMocks
    private ProductRestController productRestController;

    @Mock
    private ProductRepository productRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc= MockMvcBuilders.standaloneSetup(productRestController).build();
    }

    @Test
    public void productList() throws Exception {
        List<Zdjecia> list=new ArrayList<>();
        Zdjecia zdjecia=new Zdjecia();
        zdjecia.setAdres("adress");
        list.add(zdjecia);
        Product product=new Product(1L,"NAMEE","STATEE","HEADERR","KATEGORIAA",23,"OPISS","AUKCJAA","OWNERR","LICYTUJACYY",list);
        List<Product> productSet=new ArrayList<>();
        productSet.add(product);
        Mockito.when(productRepository.findAll()).thenReturn(productSet);
        mockMvc.perform(get("/api/products").accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())

                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].name",is(product.getName())))
                .andExpect(jsonPath("$[0].state",is(product.getState())))
                .andExpect(jsonPath("$[0].header",is(product.getHeader())))
                .andExpect(jsonPath("$[0].prcies",is(product.getPrcies())))
                .andExpect(jsonPath("$[0].opis",is(product.getOpis())))
                .andExpect(jsonPath("$[0].aukcja",is(product.getAukcja())))
                .andExpect(jsonPath("$[0].owner",is(product.getOwner())))
                .andExpect(jsonPath("$[0].licytujacy",is(product.getLicytujacy())))

        ;



    }

    @Test
    @WithMockUser(username = "OWNERR")
    public void myProduct() throws Exception {
        List<Zdjecia> list=new ArrayList<>();
        Zdjecia zdjecia=new Zdjecia();
        zdjecia.setAdres("adress");
        list.add(zdjecia);
        Product product=new Product(1L,"NAMEE","STATEE","HEADERR","KATEGORIAA",23,"OPISS","AUKCJAA","OWNERR","LICYTUJACYY",list);
        Set<Product> productSet=new HashSet<>();
        productSet.add(product);
        Mockito.when(productRepository.findByOwner(product.getOwner())).thenReturn(productSet);
        mockMvc.perform(get("/api/products/my").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].name",is(product.getName())))
                .andExpect(jsonPath("$[0].state",is(product.getState())))
                .andExpect(jsonPath("$[0].header",is(product.getHeader())))
                .andExpect(jsonPath("$[0].prcies",is(product.getPrcies())))
                .andExpect(jsonPath("$[0].opis",is(product.getOpis())))
                .andExpect(jsonPath("$[0].aukcja",is(product.getAukcja())))
                .andExpect(jsonPath("$[0].owner",is(product.getOwner())))
                .andExpect(jsonPath("$[0].licytujacy",is(product.getLicytujacy())))

        ;


    }

    @Test
    @WithMockUser(username = "OWNERR")
    public void productList1() throws Exception {

        Product product=new Product(1L,"NAMEE","STATEE","HEADERR","KATEGORIAA",23,"OPISS","AUKCJAA","OWNERR","LICYTUJACYY",null);
        Mockito.when(productRepository.findOne(product.getID())).thenReturn(product);
        mockMvc.perform(get("/api/products/{id}",1L).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.name",is(product.getName())))
                .andExpect(jsonPath("$.state",is(product.getState())))
                .andExpect(jsonPath("$.header",is(product.getHeader())))
                .andExpect(jsonPath("$.prcies",is(product.getPrcies())))
                .andExpect(jsonPath("$.opis",is(product.getOpis())))
                .andExpect(jsonPath("$.aukcja",is(product.getAukcja())))
                .andExpect(jsonPath("$.owner",is(product.getOwner())))
                .andExpect(jsonPath("$.licytujacy",is(product.getLicytujacy())))

        ;
    }




    @WithMockUser(username = "OWNERR")
    @Test
    public void meproductList() throws Exception {
        List<Zdjecia> list=new ArrayList<>();
        Zdjecia zdjecia=new Zdjecia();
        zdjecia.setAdres("adress");
        list.add(zdjecia);
        Product product=new Product(1L,"NAMEE","STATEE","HEADERR","KATEGORIAA",23,"OPISS","AUKCJAA","OWNERR","LICYTUJACYY",list);
        Set<Product> productSet=new HashSet<>();
        productSet.add(product);
        Mockito.when(productRepository.findByOwner(product.getOwner())).thenReturn(productSet);
        mockMvc.perform(get("/api/products//my/{id}",1L).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(product.getName())))
                .andExpect(jsonPath("$.state",is(product.getState())))
                .andExpect(jsonPath("$.header",is(product.getHeader())))
                .andExpect(jsonPath("$.prcies",is(product.getPrcies())))
                .andExpect(jsonPath("$.opis",is(product.getOpis())))
                .andExpect(jsonPath("$.aukcja",is(product.getAukcja())))
                .andExpect(jsonPath("$.owner",is(product.getOwner())))
                .andExpect(jsonPath("$.licytujacy",is(product.getLicytujacy())))

        ;

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(username = "OWNERR")
    public void saveProduct() throws Exception {
        Product product=new Product(1L,"NAMEE","STATEE","HEADERR","KATEGORIAA",23,"OPISS","AUKCJAA","OWNERR","LICYTUJACYY",null);
        Mockito.when(productRepository.save(product)).thenReturn(product);
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(product)))
                .andExpect(status().isOk())
        ;



    }

    @Test
    @WithMockUser(username = "OWNERR")
    public void saveProduct2() throws Exception {
        Product product=new Product(1L,"NAMEE","STATEE","HEADERR","KATEGORIAA",23,"OPISS","AUKCJAA","OWNERR","LICYTUJACYY",null);
        Mockito.when(productRepository.save(product)).thenReturn(product);
        mockMvc.perform(post("/api/products/my")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(product)))
                .andExpect(status().isOk())
        ;

    }
}