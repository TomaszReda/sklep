package pl.tomek.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class PrivacyTermsControllerTest {

    @InjectMocks
    private PrivacyTermsController privacyTermsController;

    private MockMvc mockMvc;


    @Before
    public void setUp() throws Exception {
        mockMvc= MockMvcBuilders.standaloneSetup(privacyTermsController).build();
    }

    @Test
    public void terms() throws Exception {
        mockMvc.perform(get("/terms"))
                .andExpect(status().isOk());
    }

    @Test
    public void privacy() throws Exception {
        mockMvc.perform(get("/privacy"))
                .andExpect(status().isOk());
    }
}