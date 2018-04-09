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
import pl.tomek.model.User;
import pl.tomek.model.UserRole;
import pl.tomek.repository.ProductRepository;
import pl.tomek.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
public class MyAccountControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MyAccountController myAccountController;

    private MockMvc mockMvc;

    private User user;

    @Before
    public void setUp() throws Exception {

        mockMvc= MockMvcBuilders.standaloneSetup(myAccountController).build();
        Set<UserRole> userRoleSet=new HashSet<>();
        UserRole userRole=new UserRole();
        userRole.setId(1l);
        userRole.setDescription("Domyslna rola przy rejestracji");
        userRole.setRole("DEFAULT_USER");
        userRoleSet.add(userRole);
        user=new User(2L,"imiesda","nazwiskoads","tomekreda@op.pl","loginsada","123456790",userRoleSet,false,true);
    }




    @Test
    @WithMockUser(username = "loginsada")
    public void my() throws Exception {

        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);
        mockMvc.perform(get("/myAccount"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user",user))
                .andExpect(view().name("myAccountForm"));
    }

    @Test
    @WithMockUser(username = "loginsada")
    public void zmien() throws Exception {

        user.setPassworld("123456789");
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);


        mockMvc.perform(post("/zmienHaslo")
            .param("aktualne",user.getPassworld())
                .param("nowe","123456789")
                .param("nowe2","123456789")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(view().name("myAccountForm"))
        ;

    }



    @Test
    @WithMockUser(username = "loginsada")
    public void zmienBadPassword() throws Exception {


        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);


        mockMvc.perform(post("/zmienHaslo")
                .param("aktualne",user.getPassworld())
                .param("nowe","12345673453")
                .param("nowe2","123456353")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attribute("notEquals","Hasła muszą byc identyczne"))
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(view().name("myAccountForm"))
        ;

    }

    @Test
    @WithMockUser(username = "loginsada")
    public void zmienBadPassword2() throws Exception {


        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);


        mockMvc.perform(post("/zmienHaslo")
                .param("aktualne",user.getPassworld()+"asd")
                .param("nowe","12345673453")
                .param("nowe2","123456353")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attribute("notEquals2","Hasło jest złe"))
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(view().name("myAccountForm"))
        ;

    }


    @Test
    @WithMockUser(username = "loginsada")
    public void haslo() throws Exception {

        user.setEmail("adadada@op.pl");
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);


        mockMvc.perform(post("/zmienEmail")
                .param("email","adadada@op.pl")
                .param("aktualne",user.getPassworld())

        )
                .andExpect(status().isOk())
                .andExpect(view().name("myAccountForm"));
    }



    @Test
    @WithMockUser(username = "loginsada")
    public void hasloUseEmail() throws Exception {

        Mockito.when(userRepository.findFirstByEmail(user.getEmail())).thenReturn(user);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);


        mockMvc.perform(post("/zmienEmail")
                .param("email",user.getEmail())
                .param("aktualne",user.getPassworld())

        )
                .andExpect(model().attribute("badEmail","Email juz w uzyciu jest"))
                .andExpect(status().isOk())
                .andExpect(view().name("myAccountForm"));
    }

    @Test
    @WithMockUser(username = "loginsada")
    public void haslobadPassworld() throws Exception {

        user.setEmail("adadada@op.pl");
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);


        mockMvc.perform(post("/zmienEmail")
                .param("email","adadada@op.pl")
                .param("aktualne",user.getPassworld()+"   ")

        )
                .andExpect(model().attribute("password","Złe hasło"))
                .andExpect(status().isOk())
                .andExpect(view().name("myAccountForm"));
    }




    @Test
    @WithMockUser(username = "loginsada")
    public void zmien1() throws Exception {

        Set<Product> set=new HashSet<>();
        Product product2=new Product(2L,"NAME","STATE","HEADER","KATEGORIA",23,"OPIS","AUKCJA","OWNER","LICYTUJACY",null);
        set.add(product2);
        Mockito.when(productRepository.findByOwner(user.getLogin())).thenReturn(set);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);
        user.setLogin("tomeczek");
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);

        mockMvc.perform(post("/zmienLogin")
        .param("login","tomeczek")
        )

                .andExpect(status().isOk())
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(view().name("myAccountForm"));
    }




    @Test
    @WithMockUser(username = "loginsada")
    public void zmien1loginExsist() throws Exception {

        Set<Product> set=new HashSet<>();
        Product product2=new Product(2L,"NAME","STATE","HEADER","KATEGORIA",23,"OPIS","AUKCJA","OWNER","LICYTUJACY",null);
        set.add(product2);
        Mockito.when(productRepository.findByOwner(user.getLogin())).thenReturn(set);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);
        user.setLogin("loginsada");
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);

        mockMvc.perform(post("/zmienLogin")
                .param("login","loginsada")
        )

                .andExpect(status().isOk())
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(view().name("myAccountForm"));
    }




}