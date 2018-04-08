package pl.tomek.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
public class PrivacyTermsControllerTest {

    @InjectMocks
    private PrivacyTermsController privacyTermsController;

    private MockMvc mockMvc;



    @Before

    public void setUp() throws Exception {
        mockMvc= MockMvcBuilders.standaloneSetup(privacyTermsController).build();
    }





    @WithMockUser
    @Test
    public void terms() throws Exception {
        mockMvc.perform(get("/terms"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(view().name("termsForm"));

    }

    @Test
    @WithMockUser
    public void privacy() throws Exception {
                mockMvc.perform(get("/privacy"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("nieznajomy"))
                .andExpect(view().name("privacyForm"));
    }
}