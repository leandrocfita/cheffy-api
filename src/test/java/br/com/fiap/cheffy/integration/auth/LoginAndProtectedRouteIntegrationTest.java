package br.com.fiap.cheffy.integration.auth;

import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.infrastructure.persistence.profile.repository.ProfileJpaRepository;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.repository.RestaurantJpaRepository;
import br.com.fiap.cheffy.infrastructure.persistence.user.repository.UserJpaRepository;
import br.com.fiap.cheffy.integration.helper.IntegrationTestUserHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginAndProtectedRouteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ProfileJpaRepository profileJpaRepository;

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    @BeforeEach
    void setUp() {
        restaurantJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
        IntegrationTestUserHelper.ensureProfile(profileJpaRepository, ProfileType.CLIENT);
    }

    @Test
    void shouldLoginAndAccessProtectedRouteWithJwt() throws Exception {
        String login = "maria.login";
        String password = "SenhaSegura@2026";

        IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Maria Login",
                        "maria.login@cheffy.com",
                        login,
                        password,
                        true
                )
        );

        String loginResponse = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(login, password))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = objectMapper.readTree(loginResponse).get("token").asText();

        mockMvc.perform(get("/api/v1/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        assertThat(token).isNotBlank();
    }

    @Test
    void shouldReturnUnauthorizedWhenAccessingProtectedRouteWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isUnauthorized());
    }

    private record LoginRequest(String login, String password) {
    }
}
