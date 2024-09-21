package com.jala.university.api.security;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setUp() {
        System.setProperty("LOG_ROUTE", "src/main/resources/logs/logsApi.log");
        System.setProperty("URL", "jdbc:mysql://localhost:3307/sd3");
        System.setProperty("USERNAME", "root");
        System.setProperty("PASSWORD", "sd5");
        System.setProperty("PORT", "4040");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testHttpBasicAuthenticationWorks() throws Exception {
        mockMvc.perform(get("/api/users")
                        .with(user("root").password("root")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAdminUserCanAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testRegularUserCannotAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUnauthenticatedUserCannotAccessUserEndpoints() throws Exception {
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/users"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(put("/api/users/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUnauthenticatedUserCannotAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }
}