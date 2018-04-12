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
import org.springframework.web.multipart.MultipartFile;
import pl.tomek.model.Product;
import pl.tomek.model.Zdjecia;
import pl.tomek.repository.ProductRepository;
import pl.tomek.repository.ZdjeciaRepositoru;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class MyControllerTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ZdjeciaRepositoru zdjeciaRepositoru;

    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepositoryT;
    @InjectMocks
    private  MyController myController;

    @Before
    public void setUp() throws Exception {
        mockMvc= MockMvcBuilders.standaloneSetup(myController).build();
    }


    @Test
    @WithMockUser(username = "OWNERR")
    public void my() throws Exception {
        Product product=new Product(1L,"NAMEE","STATEE","pralka sad a da","KATEGORIAA",23,"OPISS","AUKCJAA","OWNERR","LICYTUJACYY",null);
        List<Product> list=new ArrayList<>();
        list.add(product);
        productRepositoryT.save(product);
        Page<Product> all = productRepositoryT.findByOwner(product.getOwner(), new PageRequest(0, 10));

        Mockito.when(productRepository.findByOwner(product.getOwner(),new PageRequest(0,10))).thenReturn(all);


        mockMvc.perform(get("/my"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(model().attributeExists("ile"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products",all))
                .andExpect(view().name("myForm"));


    }

    @Test
    @WithMockUser
    public void usun() throws Exception {
        Mockito.doNothing().when(productRepository).delete(1L);

        mockMvc.perform(get("/usun")
            .param("ID", String.valueOf(1L))
        )
                .andExpect(view().name("redirect:my"));

    }

    @Test
    @WithMockUser
    public void detail() throws Exception {
        List<Zdjecia> list2=new ArrayList<>();
        Zdjecia zdjecia=new Zdjecia();
        zdjecia.setAdres("adress");
        list2.add(zdjecia);
        Product product=new Product(1L,"NAMEE","STATEE","HEADERR","KATEGORIAA",23,"OPISS","AUKCJAA","OWNERR","LICYTUJACYY",list2);
        List<Product> list=new ArrayList<>();
        list.add(product);
        Mockito.when(productRepository.findOne(product.getID())).thenReturn(product);


        mockMvc.perform(get("/details")
                .param("ID", String.valueOf(1L))
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("zdjecia"))
                .andExpect(view().name("myDetails"));
    }






    @Test
    @WithMockUser
    public void edytuj() throws Exception {

        List<Zdjecia> list2=new ArrayList<>();
        Zdjecia zdjecia=new Zdjecia();
        zdjecia.setAdres("adress");
        list2.add(zdjecia);
        Product product=new Product(1L,"NAMEE","STATEE","HEADERR","KATEGORIAA",23,"OPISS","AUKCJAA","OWNERR","LICYTUJACYY",list2);
        List<Product> list=new ArrayList<>();
        list.add(product);
        Mockito.when(productRepository.findOne(product.getID())).thenReturn(product);


        mockMvc.perform(get("/edytuj")
                .param("ID", String.valueOf(1L))
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("zdjecia"))
                .andExpect(model().attributeExists("ID"))
                .andExpect(view().name("EditDetailsForm"));


    }

    @Test
    @WithMockUser(username = "OWNERR")
    public void edytuj1() throws Exception {
        List<Zdjecia> list=new ArrayList<>();
        Zdjecia zdjecia=new Zdjecia();
        zdjecia.setAdres("adress");
        list.add(zdjecia);
        Product product=new Product(1L,"NAMEE","STATEE","HEADERR","KATEGORIAA",23,"OPISS","AUKCJAA","OWNERR","LICYTUJACYY",list);
        MultipartFile multipartFilee=new MultipartFile() {
            @Override
            public String getName() {
                return "tomek2ss";
            }

            @Override
            public String getOriginalFilename() {
                return "tomek3ss";
            }

            @Override
            public String getContentType() {
                return "tomek4ss";
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 4;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File file) throws IOException, IllegalStateException {

            }
        };
        Mockito.when(productRepository.save(product)).thenReturn(product);
        MultipartFile[] file=new MultipartFile[1];
        file[0]=multipartFilee;
        mockMvc.perform(post("/edytuj")
                .param("ID", String.valueOf(product.getID()))
                .param("file", String.valueOf(file))
                .param("prcies", String.valueOf(product.getPrcies()))
                .param("aukcja",product.getAukcja())
                .param("header",product.getHeader())
                .param("kategoria",product.getKategoria())
                .param("licytujacy",product.getLicytujacy())
                .param("name",product.getName())
                .param("opis",product.getOpis())
                .param("owner",product.getOwner())
                .param("state",product.getState())

        )

                .andExpect(view().name("redirect:details?ID=1"));





    }
}