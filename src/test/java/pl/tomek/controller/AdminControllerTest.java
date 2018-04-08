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
import pl.tomek.model.Product;
import pl.tomek.model.User;
import pl.tomek.model.UserRole;
import pl.tomek.repository.ProductRepository;
import pl.tomek.repository.UserRepository;
import pl.tomek.repository.UserRoleRepository;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class AdminControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AdminController adminController;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleRepository userRoleRepository;

    @Before
    public void setUp() throws Exception {
        mockMvc= MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    @WithMockUser(username = "Admin",password = "123456")
    public void admin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminForm"))
                .andExpect(model().attributeExists("isAdmin"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(model().attribute("nieznajomy","anonymousUser"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    @WithMockUser(username = "Admin",password = "123456")
    public void delete() throws Exception {

        Product product=new Product(1L,"NAMEE","STATEE","HEADERR","KATEGORIAA",23,"OPISS","AUKCJAA","OWNERR","LICYTUJACYY",null);
        productRepository.save(product);
        String header=product.getHeader();
        Mockito.when(productRepository.findFirstByHeader(header)).thenReturn(product);
        Mockito.doNothing().when(productRepository).delete(product);

        mockMvc.perform(post("/deleteProduct").with(csrf())
            .param("nazwa",header))
                .andExpect(view().name("redirect:admin"))
                ;
    }

    @Test
    @WithMockUser(username = "Admin",password = "123456")
    public void usun() throws Exception {

        Set<UserRole> userRoleSet=new HashSet<>();
        UserRole userRole=new UserRole();
        userRole.setDescription("Domyslna rola przy rejestracji");
        userRole.setRole("DEFAULT_ROLE");
        userRoleSet.add(userRole);
        UserRole admin=new UserRole();
        admin.setRole("ADMIN ROLE");
        admin.setDescription("sadada");
        Product product=new Product(2L,"NAME","STATE","HEADER","KATEGORIA",23,"OPIS","AUKCJA","login","LICYTUJACY",null);
        Set<Product> products=new HashSet<>();
        products.add(product);
        User user=new User(2L,"imie","nazwisko","tomekreda@op.pl","login","passworld",userRoleSet,true,true);


        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);
        Mockito.when(userRoleRepository.findByRole(admin.getRole())).thenReturn(admin);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.doNothing().when(userRepository).delete(user);
        Mockito.when(productRepository.findByOwner(user.getLogin())).thenReturn(products);
        Mockito.doNothing().when(productRepository).delete(product);


        mockMvc.perform(post("/deleteUser").with(csrf())
        .param("nazwa",user.getLogin()))
                .andExpect(view().name("redirect:admin"))
                ;



    }

    @Test
    @WithMockUser(username = "Admin",password = "123456")
    public void deleteAdmin() throws Exception {

        Set<UserRole> userRoleSet=new HashSet<>();
        UserRole userRole=new UserRole();
        userRole.setDescription("Domyslna rola przy rejestracji");
        userRole.setRole("ADMIN ROLE");
        userRoleSet.add(userRole);
        UserRole admin=new UserRole();
        admin.setRole("ADMIN ROLE");
        admin.setDescription("sadada");
        User user=new User(2L,"imie","nazwisko","tomekreda@op.pl","login","passworld",userRoleSet,true,true);


        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);
        Mockito.when(userRoleRepository.findByRole(admin.getRole())).thenReturn(admin);
        Mockito.when(userRepository.save(user)).thenReturn(user);


        mockMvc.perform(post("/deleteUser").with(csrf())
                .param("nazwa",user.getLogin()))
                .andExpect(view().name("redirect:admin"))
        ;



    }


}