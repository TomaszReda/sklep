package pl.tomek.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


@RunWith(SpringJUnit4ClassRunner.class)
public class HomeControllerTest {

    @InjectMocks
    private HomeController homeController;

    private MockMvc mockMvc;



    @Before
    public void setUp() throws Exception {
        mockMvc= MockMvcBuilders.standaloneSetup(homeController).build();


    }


    @Test
    @WithMockUser
    public void home() throws Exception {

       mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
        .andExpect(model().attributeExists("nieznajomy"))
        .andExpect(model().attributeExists("username"))
        .andExpect(model().attributeExists("nieznajomy"))
        ;
    }
}