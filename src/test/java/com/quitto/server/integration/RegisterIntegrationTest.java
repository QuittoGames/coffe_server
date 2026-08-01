package com.quitto.server.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quitto.server.application.dto.Auth.LoginDTO;
import com.quitto.server.application.dto.Auth.RegisterDTO;
import com.quitto.server.domain.interfaces.Token.TokenService;
import com.quitto.server.infrastructure.db.User.Entity.UserEntity;
import com.quitto.server.infrastructure.db.User.Repository.JpaUserRepository;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@ActiveProfiles("test")
class RegisterIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private TokenService<Long> tokenService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void tearDown() {
        // Limpa usuários criados nos testes
        String[] testUsers = {
            "new_user_reg", "db_check_user", "register_then_login",
            "dup_user", "second_user"
        };
        for (String name : testUsers) {
            userRepository.findByName(name).ifPresent(u ->
                userRepository.deleteById(u.getId()));
        }
    }

    @Test
    void register_withValidData_returns200AndSetsCookie() throws Exception {
        RegisterDTO register = new RegisterDTO("new_user_reg", "SenhaForte123!", "new_reg@email.com");

        MvcResult result = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
            .andExpect(status().isOk())
            .andReturn();

        Cookie cookie = result.getResponse().getCookie("access_token");
        assertNotNull(cookie, "access_token cookie must be present");
        assertFalse(cookie.getValue().isBlank());
        assertTrue(tokenService.verifyToken(cookie.getValue()));
    }

    @Test
    void register_createsUserInDatabase() throws Exception {
        RegisterDTO register = new RegisterDTO("db_check_user", "Senha123!", "db_check@email.com");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
            .andExpect(status().isOk());

        var savedUser = userRepository.findByName("db_check_user");
        assertTrue(savedUser.isPresent());
        assertEquals("db_check_user", savedUser.get().getName());
        assertEquals("db_check@email.com", savedUser.get().getEmail());
    }

    @Test
    void register_withEmptyPassword_returns401() throws Exception {
        RegisterDTO register = new RegisterDTO("user_no_pass", "", "nopass@email.com");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void newlyRegisteredUser_canLogin() throws Exception {
        RegisterDTO register = new RegisterDTO("register_then_login", "MinhaSenha123!", "rtl@email.com");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
            .andExpect(status().isOk());

        LoginDTO login = new LoginDTO("register_then_login", "MinhaSenha123!");
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isOk());
    }
}
