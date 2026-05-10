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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    void shouldReturnUnauthorizedWhenAccessingProtectedRouteWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isUnauthorized());
    }

    private record LoginRequest(String login, String password) {
    }
}
