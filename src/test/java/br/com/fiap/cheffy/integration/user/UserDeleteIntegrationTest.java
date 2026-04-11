package br.com.fiap.cheffy.integration.user;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserDeleteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    @Autowired
    private ProfileJpaRepository profileJpaRepository;

    @BeforeEach
    void setUp() {
        restaurantJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
        IntegrationTestUserHelper.ensureProfile(profileJpaRepository, ProfileType.CLIENT);
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldDeactivateUserSuccessfully() throws Exception {
        // 1. Create a user
        String userIdStr = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Usuario Para Deletar",
                        "usuario.delete@cheffy.com",
                        "usuario.delete",
                        "SenhaSegura@2026",
                        true
                )
        );
        UUID userId = UUID.fromString(userIdStr);

        // 2. Deactivate the user
        mockMvc.perform(patch("/api/v1/users/{id}/deactivate", userId))
                .andExpect(status().isNoContent());

        // 3. Verify in DB
        var deactivatedUser = userJpaRepository.findById(userId);
        assertThat(deactivatedUser).isPresent();
        assertThat(deactivatedUser.get().isActive()).isFalse();
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldReturnNotFoundWhenDeactivatingNonExistentUser() throws Exception {
        mockMvc.perform(patch("/api/v1/users/{id}/deactivate", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}
