package pl.tomek.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.tomek.model.Product;
import pl.tomek.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

import  static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)

@DataJpaTest
public class SearchControllerTest {

    @InjectMocks
    private SearchController searchController;

    @Mock
    private ProductRepository productRepository;

    @Autowired
    private ProductRepository productRepositoryT;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc= MockMvcBuilders.standaloneSetup(searchController).build();

    }

    @Test
    @WithMockUser
    public void saerch() throws Exception {

        Product product=new Product(1L,"NAMEE","STATEE","HEADERR","KATEGORIAA",23,"OPISS","AUKCJAA","OWNERR","LICYTUJACYY",null);
        List<Product> list=new ArrayList<>();
        list.add(product);

        productRepositoryT.save(product);
        Page<Product> page = productRepositoryT.findAll(new PageRequest(0,10));



        Mockito.when(productRepository.findAll(new PageRequest(0,10))).thenReturn(page);


        mockMvc.perform(get("/search"))
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(model().attributeExists("ile"))
                .andExpect(model().attributeExists("products"))
                .andExpect(status().isOk())
                .andExpect(view().name("searchForm"));
    }

    @Test
    @WithMockUser
    public void precisionSearch() throws Exception {
        Product product=new Product(1L,"NAMEE","STATEE","pralka sad a da","KATEGORIAA",23,"OPISS","AUKCJAA","OWNERR","LICYTUJACYY",null);
        List<Product> list=new ArrayList<>();
        list.add(product);

        Mockito.when(productRepository.findAll()).thenReturn(list);
        Mockito.when(productRepository.findAllByStateAndKategoria("test","test")).thenReturn(list);

        mockMvc.perform(get("/searchh")
                        .param("word","pralka")
                        .param("filtr","cenar")
                        .param("kategoria","test")
                        .param("stan","test")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(model().attributeExists("ile"))
                .andExpect(model().attributeExists("products"))
                .andExpect(view().name("searchForm"));



    }
}