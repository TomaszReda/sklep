package pl.tomek.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.tomek.model.User;
import pl.tomek.model.UserRole;
import pl.tomek.repository.UserRepository;
import pl.tomek.service.UserService;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class RegisterControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegisterController registerController;

    private MockMvc mockMvc;


    @Before
    public void setUp() throws Exception {
        mockMvc= MockMvcBuilders.standaloneSetup(registerController).build();
    }




    @Test
    public void saveUser() throws Exception {


        Set<UserRole> userRoleSet=new HashSet<>();
        UserRole userRole=new UserRole();
        userRole.setId(1l);
        userRole.setDescription("Domyslna rola przy rejestracji");
        userRole.setRole("DEFAULT_USER");
        userRoleSet.add(userRole);
        User users=new User(2L,"imiesda","nazwiskoads","tomekreda@op.pl","loginsada","123456790",userRoleSet,true,true);

        Mockito.doNothing().when(userService).addwithDefaultRole(users);


        mockMvc.perform(post("/register")
                .param("id", users.getId()+"")
                .param("firstname",users.getFirstname())
                .param("lastname",users.getLastname())
                .param("email",users.getEmail())
                .param("login",users.getLogin())
                .param("passworld",users.getPassworld())
                .param("regulamin1", String.valueOf(users.isRegulamin1()))
                .param("regulamin2", String.valueOf(users.isRegulamin2())))

                .andExpect(status().isOk())
                .andExpect(view().name("succesForm"));



    }

    @Test
    public void noValidDate() throws Exception {
        Set<UserRole> userRoleSet=new HashSet<>();
        UserRole userRole=new UserRole();
        userRole.setId(1l);
        userRole.setDescription("Domyslna rola przy rejestracji");
        userRole.setRole("DEFAULT_USER");
        userRoleSet.add(userRole);
        User users=new User(2L,"imi","naz","tomekreda@op.pl","log","1234",userRoleSet,true,true);

        Mockito.doNothing().when(userService).addwithDefaultRole(users);


        mockMvc.perform(post("/register")
                .param("id", users.getId()+"")
                .param("firstname",users.getFirstname())
                .param("lastname",users.getLastname())
                .param("email",users.getEmail())
                .param("login",users.getLogin())
                .param("passworld",users.getPassworld())
                .param("regulamin1", String.valueOf(users.isRegulamin1()))
                .param("regulamin2", String.valueOf(users.isRegulamin2())))

                .andExpect(status().isOk())
                .andExpect(view().name("registerForm"));



    }

    @Test
    public void noAcceptRegulamin() throws Exception {
        Set<UserRole> userRoleSet=new HashSet<>();
        UserRole userRole=new UserRole();
        userRole.setId(1l);
        userRole.setDescription("Domyslna rola przy rejestracji");
        userRole.setRole("DEFAULT_USER");
        userRoleSet.add(userRole);
        User user=new User(2L,"imiesda","nazwiskoads","tomekreda@op.pl","loginsada","123456790",userRoleSet,false,true);

        Mockito.doNothing().when(userService).addwithDefaultRole(user);


        mockMvc.perform(post("/register")
                .param("id", user.getId()+"")
                .param("firstname",user.getFirstname())
                .param("lastname",user.getLastname())
                .param("email",user.getEmail())
                .param("login",user.getLogin())
                .param("passworld",user.getPassworld())
                .param("regulamin1", String.valueOf(user.isRegulamin1()))
                .param("regulamin2", String.valueOf(user.isRegulamin2())))

                .andExpect(status().isOk())
                .andExpect(view().name("registerForm"));



    }




    @Test
    public void register() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("registerForm"));



    }


}