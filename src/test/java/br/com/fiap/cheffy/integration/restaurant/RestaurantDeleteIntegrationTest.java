package br.com.fiap.cheffy.integration.restaurant;

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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RestaurantDeleteIntegrationTest {

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
        IntegrationTestUserHelper.ensureProfile(profileJpaRepository, ProfileType.OWNER);
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldDeactivateRestaurantSuccessfully() throws Exception {
        // 1. Create User
        String userId = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Dono Deactivate",
                        "dono.deactivate@cheffy.com",
                        "dono.deactivate",
                        "SenhaSegura@2026",
                        true
                )
        );

        // 2. Create Restaurant
        var restaurantRequest = new RestaurantCreateRequest(
                "Restaurante a Deletar",
                "Italiana",
                "11.222.333/0001-81",
                "08:00",
                "22:00",
                "America/Sao_Paulo",
                false,
                new RestaurantAddressCreateRequest(
                        "Rua do Fim",
                        10,
                        "São Paulo",
                        "01001000",
                        "Centro",
                        "SP",
                        "Sala 1"
                )
        );

        String restaurantIdStr = mockMvc.perform(post("/api/v1/restaurants/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        UUID restaurantId = UUID.fromString(restaurantIdStr);

        // 3. Deactivate Restaurant
        mockMvc.perform(patch("/api/v1/restaurants/{id}/deactivate", restaurantId)
                        .param("userId", userId))
                .andExpect(status().isNoContent());

        // 4. Verify in DB
        var deactivatedRestaurant = restaurantJpaRepository.findById(restaurantId);
        assertThat(deactivatedRestaurant).isPresent();
        assertThat(deactivatedRestaurant.get().getActive()).isFalse();
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldReturnForbiddenWhenUserIsNotOwnerTryingToDeactivate() throws Exception {
        // 1. Create User 1 (Owner)
        String user1Id = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Dono Real Delete",
                        "dono.real.del@cheffy.com",
                        "dono.real.del",
                        "SenhaSegura@2026",
                        true
                )
        );

        // 2. Create User 2 (Fake Owner)
        String user2Id = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Dono Falso Delete",
                        "dono.falso.del@cheffy.com",
                        "dono.falso.del",
                        "SenhaSegura@2026",
                        true
                )
        );

        // 3. Create Restaurant for User 1
        var restaurantRequest = new RestaurantCreateRequest(
                "Restaurante Intocavel Delete",
                "Japonesa",
                "11.222.333/0001-81",
                "08:00",
                "22:00",
                "America/Sao_Paulo",
                false,
                new RestaurantAddressCreateRequest(
                        "Rua Intocavel",
                        100,
                        "São Paulo",
                        "01001000",
                        "Centro",
                        "SP",
                        "Sala 1"
                )
        );

        String restaurantIdStr = mockMvc.perform(post("/api/v1/restaurants/{userId}", user1Id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 4. Try to deactivate using User 2 (Should Fail with Forbidden/Conflict depending on domain logic)
        mockMvc.perform(patch("/api/v1/restaurants/{id}/deactivate", restaurantIdStr)
                        .param("userId", user2Id))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldReturnNotFoundWhenDeactivatingNonExistentRestaurant() throws Exception {
         // 1. Create a User
        String userId = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Dono Solitario Delete",
                        "dono.solitario.del@cheffy.com",
                        "dono.solitario.del",
                        "SenhaSegura@2026",
                        true
                )
        );

        // 2. Try to deactivate a random restaurant ID
        mockMvc.perform(patch("/api/v1/restaurants/{id}/deactivate", UUID.randomUUID())
                        .param("userId", userId))
                .andExpect(status().isNotFound());
    }

    private record RestaurantCreateRequest(
            String name,
            String culinary,
            String cnpj,
            String openingTime,
            String closingTime,
            String zoneId,
            boolean open24hours,
            RestaurantAddressCreateRequest address
    ) {}

    private record RestaurantAddressCreateRequest(
            String streetName,
            Integer number,
            String city,
            String postalCode,
            String neighborhood,
            String stateProvince,
            String addressLine
    ) {}
}
