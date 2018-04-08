package pl.tomek.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;


import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    private MockMvc mockMvc;



    @Before
    public void setUp() throws Exception {
    mockMvc= MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    @WithMockUser
    public void loginForm() throws Exception {
        mockMvc.perform(get("/loginform"))
                .andExpect(status().isOk());
    }

    @Test
    public void errors() throws Exception {
        mockMvc.perform(get("/formError"))
                .andExpect(status().isOk());
    }

    @Test
    public void verfiLogin() throws Exception {
        mockMvc.perform(formLogin("/processinglogin").user("admin").password("pass"));
    }

    @Test
    public void verfiLogout() throws Exception {
        mockMvc.perform(logout());

    }
}