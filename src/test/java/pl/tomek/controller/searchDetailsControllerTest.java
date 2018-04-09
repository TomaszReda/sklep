package pl.tomek.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.tomek.model.Product;
import pl.tomek.model.Zdjecia;
import pl.tomek.repository.ProductRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class searchDetailsControllerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private searchDetailsController searchDetailsController;

    private MockMvc mockMvc;


    private Product product;


    @Before
    public void setUp() throws Exception {
        mockMvc= MockMvcBuilders.standaloneSetup(searchDetailsController).build();
        List<Zdjecia> list2=new ArrayList<>();
        Zdjecia zdjecia=new Zdjecia();
        zdjecia.setAdres("adress");
        list2.add(zdjecia);
        product=new Product(1L,"NAMEE","STATEE","HEADERR","KATEGORIAA",23,"OPISS","AUKCJAA","OWNERR","LICYTUJACYY",list2);

    }

    @Test
    @WithMockUser
    public void details() throws Exception {

        Mockito.when(productRepository.findOne(product.getID())).thenReturn(product);
        Mockito.when(productRepository.save(product)).thenReturn(product);

        Set<String> list=new HashSet<>();
        list.add(product.getZdjecia().get(0).getAdres());

        mockMvc.perform(get("/detailsSearch")
                .param("ID", String.valueOf(1L))
        )
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("zdjecia"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("zdjecia",list))
                .andExpect(model().attribute("product",product))
                .andExpect(view().name("searchDetails"));
    }

    @Test
    @WithMockUser
    public void buy() throws Exception {

        Mockito.doNothing().when(productRepository).delete(product.getID());


        mockMvc.perform(get("/buy")
                .param("ID", String.valueOf(product.getID()))
        )

                .andExpect(view().name("redirect:succesBought"));
    }

    @Test
    @WithMockUser
    public void succes() throws Exception {

        mockMvc.perform(get("/succesBought"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("username"))
                .andExpect(view().name("succesBoughtForm"));
    }

    @Test
    @WithMockUser(username = "OWNERR")
    public void oferta() throws Exception {

       Mockito.when(productRepository.findOne(product.getID())).thenReturn(product);

        mockMvc.perform(post("/oferta")
                .param("ID", String.valueOf(product.getID()))
                .param("cena", String.valueOf(product.getPrcies()))
        )


                .andExpect(view().name("redirect:/detailsSearch?ID=1"));
    }
}