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
public class RestaurantUpdateIntegrationTest {

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
    void shouldUpdateRestaurantSuccessfully() throws Exception {
        // 1. Create a User
        String userId = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Dono Restaurante",
                        "dono.update@cheffy.com",
                        "dono.update",
                        "SenhaSegura@2026",
                        true
                )
        );

        // 2. Create a Restaurant
        var restaurantRequest = new RestaurantCreateRequest(
                "Restaurante Velho",
                "Italiana",
                "11.222.333/0001-81",
                "08:00",
                "22:00",
                "America/Sao_Paulo",
                false,
                new RestaurantAddressCreateRequest(
                        "Rua Antiga",
                        200,
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

        // 3. Update the Restaurant
        var updateRequest = new RestaurantUpdateDTO(
                "Restaurante Novo",
                "Brasileira",
                "09:00",
                "23:00",
                "America/Sao_Paulo",
                true
        );

        mockMvc.perform(patch("/api/v1/restaurants/{id}", restaurantId)
                        .param("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNoContent());

        // 4. Verify in DB
        var updatedRestaurant = restaurantJpaRepository.findById(restaurantId);
        assertThat(updatedRestaurant).isPresent();
        assertThat(updatedRestaurant.get().getName()).isEqualTo("Restaurante Novo");
        assertThat(updatedRestaurant.get().getCulinary()).isEqualTo("Brasileira");
        assertThat(updatedRestaurant.get().isOpen24hours()).isTrue();
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldReturnConflictWhenUserIsNotOwnerTryingToUpdate() throws Exception {
        // 1. Create User 1 (Owner)
        String user1Id = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Dono Verdadeiro",
                        "dono.real@cheffy.com",
                        "dono.real",
                        "SenhaSegura@2026",
                        true
                )
        );

        // 2. Create User 2 (Fake Owner)
        String user2Id = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Dono Falso",
                        "dono.falso@cheffy.com",
                        "dono.falso",
                        "SenhaSegura@2026",
                        true
                )
        );

        // 3. Create Restaurant for User 1
        var restaurantRequest = new RestaurantCreateRequest(
                "Restaurante Intocavel",
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

        // 4. Try to update using User 2 (Should Fail with Forbidden/Conflict depending on domain logic)
        var updateRequest = new RestaurantUpdateDTO(
                "Restaurante Roubado",
                "Brasileira",
                "09:00",
                "23:00",
                "America/Sao_Paulo",
                true
        );

        mockMvc.perform(patch("/api/v1/restaurants/{id}", restaurantIdStr)
                        .param("userId", user2Id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldReturnNotFoundWhenUpdatingNonExistentRestaurant() throws Exception {
         // 1. Create a User
        String userId = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Dono Sozinho",
                        "dono.sozinho@cheffy.com",
                        "dono.sozinho",
                        "SenhaSegura@2026",
                        true
                )
        );

        // 2. Try to update a random restaurant ID
        var updateRequest = new RestaurantUpdateDTO(
                "Restaurante Fantasma",
                "Brasileira",
                "09:00",
                "23:00",
                "America/Sao_Paulo",
                true
        );

        mockMvc.perform(patch("/api/v1/restaurants/{id}", UUID.randomUUID())
                        .param("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
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

    private record RestaurantUpdateDTO(
            String name,
            String culinary,
            String openingTime,
            String closingTime,
            String zoneId,
            Boolean open24hours
    ) {}

    private record RestaurantAddressUpdateDTO(
            String streetName,
            Integer number,
            String city,
            String postalCode,
            String neighborhood,
            String stateProvince,
            String addressLine
    ) {}
}