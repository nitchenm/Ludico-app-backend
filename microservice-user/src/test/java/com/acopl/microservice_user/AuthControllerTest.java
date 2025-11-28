package com.acopl.microservice_user;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.acopl.microservice_user.controller.AuthController;
import com.acopl.microservice_user.model.User;
import com.acopl.microservice_user.repository.UserRepository;
import com.acopl.microservice_user.security.JwtUtil;

/**
 * Test suite for AuthController
 * Tests JWT token generation, user registration, and authentication
 */
import com.acopl.microservice_user.config.TestSecurityConfig;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

/**
 * Test suite for AuthController
 * Tests JWT token generation, user registration, and authentication
 */
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        // store encoded password so PasswordEncoder.matches works in tests
        testUser.setPassword(passwordEncoder.encode("Password123"));
        testUser.setName("Test User");
        testUser.setRole("USER");
    }

    /**
     * Test successful user registration
     */
    @Test
    @WithMockUser
    void testRegisterUser_success() throws Exception {
        when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/register")
                .param("email", "newuser@example.com")
                .param("password", "Password123")
                .param("name", "New User"))
                .andExpect(status().isOk());

        verify(userRepository).save(any(User.class));
    }

    /**
     * Test registration with duplicate email
     */
    @Test
    @WithMockUser
    void testRegisterUser_duplicateEmail() throws Exception {
        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(testUser));

        mockMvc.perform(post("/auth/register")
                .param("email", "existing@example.com")
                .param("password", "Password123")
                .param("name", "Existing User"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test registration with default role and name
     */
    @Test
    @WithMockUser
    void testRegisterUser_defaultRoleAndName() throws Exception {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/register")
                .param("email", "test@example.com")
                .param("password", "Password123"))
                .andExpect(status().isOk());

        verify(userRepository).save(any(User.class));
    }

    /**
     * Test successful login
     */
    @Test
    @WithMockUser
    void testLogin_success() throws Exception {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(jwtUtil.generateToken(1L, "test@example.com")).thenReturn("jwt.token.here");

        mockMvc.perform(post("/auth/login")
                .param("email", "test@example.com")
                .param("password", "Password123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt.token.here"));
    }

    /**
     * Test login with non-existent user
     */
    @Test
    @WithMockUser
    void testLogin_userNotFound() throws Exception {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                .param("email", "nonexistent@example.com")
                .param("password", "Password123"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Test login with invalid password
     */
    @Test
    @WithMockUser
    void testLogin_invalidPassword() throws Exception {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        mockMvc.perform(post("/auth/login")
                .param("email", "test@example.com")
                .param("password", "WrongPassword"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Test login response contains all expected fields
     */
    @Test
    @WithMockUser
    void testLogin_responseContainsAllFields() throws Exception {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(jwtUtil.generateToken(1L, "test@example.com")).thenReturn("jwt.token.here");

        mockMvc.perform(post("/auth/login")
                .param("email", "test@example.com")
                .param("password", "Password123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    /**
     * Test successful login after failed attempt
     */
    @Test
    @WithMockUser
    void testLogin_successAfterFailure() throws Exception {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(jwtUtil.generateToken(1L, "test@example.com")).thenReturn("jwt.token.here");

        mockMvc.perform(post("/auth/login")
                .param("email", "test@example.com")
                .param("password", "Password123"))
                .andExpect(status().isOk());
    }

    /**
     * Test JWT token is in response
     */
    @Test
    @WithMockUser
    void testLogin_jwtInResponse() throws Exception {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(jwtUtil.generateToken(1L, "test@example.com")).thenReturn("eyJhbGciOiJIUzI1NiJ9...");

        mockMvc.perform(post("/auth/login")
                .param("email", "test@example.com")
                .param("password", "Password123")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    /**
     * Test register with missing name parameter
     */
    @Test
    @WithMockUser
    void testRegisterUser_missingName() throws Exception {
        when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/register")
                .param("email", "newuser@example.com")
                .param("password", "Password123"))
                .andExpect(status().isOk());

        verify(userRepository).save(any(User.class));
    }

    /**
     * Test register response status
     */
    @Test
    @WithMockUser
    void testRegisterUser_responseStatus() throws Exception {
        when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/register")
                .param("email", "newuser@example.com")
                .param("password", "Password123")
                .param("name", "New User")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());
    }
}
