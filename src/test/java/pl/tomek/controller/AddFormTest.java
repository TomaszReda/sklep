package pl.tomek.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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

import  static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)

public class AddFormTest {

    @InjectMocks
    private AddForm addForm;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ZdjeciaRepositoru zdjeciaRepositoru;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc= MockMvcBuilders.standaloneSetup(addForm).build();
    }

    @Test
    @WithMockUser(username = "tomek",password = "GHJbnm123")
    public void add() throws Exception {

        mockMvc.perform(get("/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("username","tomek"));
    }



    @Test
    @WithMockUser(username = "tomek",password = "GHJbnm123")
    public void succes() throws Exception {
        mockMvc.perform(get("/succes"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attribute("username","tomek"));
    }




    @Test
    @WithMockUser
    public void dodajError() throws Exception {

        List<Zdjecia> list=new ArrayList<>();
        Zdjecia zdjecia=new Zdjecia();
        zdjecia.setAdres("adress");
        list.add(zdjecia);
        Product product=new Product(1L,"NAMEE","STATEE","HEADERR","KATEGORIAA",23,"OPISS","AUKCJAA","OWNERR","LICYTUJACYY",list);
        MultipartFile multipartFile=new MultipartFile() {
            @Override
            public String getName() {
                return "tomek";
            }

            @Override
            public String getOriginalFilename() {
                return "tomek";
            }

            @Override
            public String getContentType() {
                return "tomek";
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
        MultipartFile[] file=new MultipartFile[1];
        file[0]=multipartFile;
        Mockito.when(productRepository.save(product)).thenReturn(product);


        mockMvc.perform(post("/add")
                .param("ID", String.valueOf(product.getID()))
                .param("file", String.valueOf(file))

                .param("aukcja",product.getAukcja())
                .param("header",product.getHeader())
                .param("kategoria",product.getKategoria())
                .param("licytujacy",product.getLicytujacy())
                .param("name",product.getName())
                .param("opis",product.getOpis())
                .param("owner",product.getOwner())
                .param("state",product.getState())

        )
                .andExpect(status().isOk())
                .andExpect(view().name("addForm"));


    }



    @Test
    @WithMockUser(username = "OWNERR")
    public void dodaj() throws Exception {

        List<Zdjecia> list=new ArrayList<>();
        Zdjecia zdjecia=new Zdjecia();
        zdjecia.setAdres("adress");
        list.add(zdjecia);
        Product product=new Product(1L,"NAMEE","STATEE","HEADERR","KATEGORIAA",23,"OPISS","AUKCJAA","OWNERR","LICYTUJACYY",list);
        MultipartFile multipartFilee=new MultipartFile() {
            @Override
            public String getName() {
                return "tomek2";
            }

            @Override
            public String getOriginalFilename() {
                return "tomek3";
            }

            @Override
            public String getContentType() {
                return "tomek4";
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
        MultipartFile[] file=new MultipartFile[1];
        file[0]=multipartFilee;
        Mockito.when(productRepository.save(product)).thenReturn(product);


        mockMvc.perform(post("/add")
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

                .andExpect(view().name("redirect:/succes"));


    }



}