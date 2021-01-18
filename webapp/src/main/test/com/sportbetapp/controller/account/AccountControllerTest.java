package com.sportbetapp.controller.account;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.sportbetapp.Application;
import com.sportbetapp.config.H2TestProfileJPAConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        Application.class,
        H2TestProfileJPAConfig.class
},
        properties = {"spring.datasource.initialization-mode=never"})
@ActiveProfiles("test")
@Sql(
        scripts = "/testsql/test-user-data.sql",
        config = @SqlConfig(commentPrefix = "#", separator = ";")
)
@Transactional
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenTryingToAccessUserPrivileges_withGuestRight_thenAskToLogin() throws Exception {
        this.mockMvc.perform(get("/user/home"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails("client@gmail.com")
    public void whenTryingToAccessInspectorPrivileges_withClientRight_thenShow403ErrorPage() throws Exception {
        this.mockMvc.perform(get("/admin/upload-template"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    public void whenTryingToAccessClientPrivileges_withInspectorRight_thenAllow() throws Exception {
        this.mockMvc.perform(get("/user/home"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails("client@gmail.com")
    public void whenTryingToAccessClientPrivileges_withCorrespondingRight_thenSuccess() throws Exception {
        this.mockMvc.perform(get("/user/home"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    public void whenTryingToAccessInspectorPrivileges_withCorrespondingRight_thenSuccess() throws Exception {
        this.mockMvc.perform(get("/admin/predict"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

}
