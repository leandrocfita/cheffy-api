package br.com.fiap.cheffy.integration.fooditem;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.infrastructure.persistence.fooditem.repository.FoodItemJpaRepository;
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

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FoodItemUpdateIntegrationTest {

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

    @Autowired
    private FoodItemJpaRepository foodItemJpaRepository;

    @BeforeEach
    void setUp() {
        foodItemJpaRepository.deleteAll();
        restaurantJpaRepository.deleteAll();
        userJpaRepository.deleteAll();

        IntegrationTestUserHelper.ensureProfile(profileJpaRepository, ProfileType.CLIENT);
        IntegrationTestUserHelper.ensureProfile(profileJpaRepository, ProfileType.OWNER);
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldUpdateFoodItemFromHttpRequestAndPersistInDatabase() throws Exception {
        // 1. Create a User
        String userId = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Joao Chef",
                        "joao.chef@cheffy.com",
                        "joao.chef",
                        "SenhaSegura@2026",
                        true
                )
        );

        // 2. Create a Restaurant for the User
        var restaurantRequest = new RestaurantCreateRequest(
                "Restaurante do Joao",
                "Brasileira",
                "11.222.333/0001-81",
                "08:00",
                "22:00",
                "America/Sao_Paulo",
                false,
                new RestaurantAddressCreateRequest(
                        "Rua dos Restaurantes",
                        201,
                        "São Paulo",
                        "01001000",
                        "Centro",
                        "SP",
                        "Sala 2"
                )
        );

        String createdRestaurantId = mockMvc.perform(post("/api/v1/restaurants/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 3. Create a Food Item for the Restaurant
        var foodItemRequest = new FoodItemCreateRequest(
                "Feijoada",
                "Feijoada completa",
                new BigDecimal("60.00"),
                "photo-feijoada-123",
                true,
                true
        );

        String responseBody = mockMvc.perform(post("/api/v1/restaurants/{restaurantId}/food-items", createdRestaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodItemRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        FoodItemQueryPort queryPort = objectMapper.readValue(responseBody, FoodItemQueryPort.class);

        // 4. Update the Food Item
        var foodItemUpdateRequest = new FoodItemCreateRequest(
                "Feijoada Light",
                "Feijoada com carnes magras",
                new BigDecimal("65.00"),
                "photo-feijoada-light-123",
                false,
                false
        );

        mockMvc.perform(patch("/api/v1/restaurants/{restaurantId}/food-items/{foodItemId}", createdRestaurantId, queryPort.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodItemUpdateRequest))
                        .requestAttr("userId", UUID.fromString(userId))) // Simulating the userId attribute set by auth filter
                .andExpect(status().isNoContent());

        // 5. Verify in Database
        var updatedFoodItem = foodItemJpaRepository.findById(queryPort.id());
        assertThat(updatedFoodItem).isPresent();
        assertThat(updatedFoodItem.get().getName()).isEqualTo("Feijoada Light");
        assertThat(updatedFoodItem.get().getDescription()).isEqualTo("Feijoada com carnes magras");
        assertThat(updatedFoodItem.get().getPrice()).isEqualByComparingTo("65.00");
        assertThat(updatedFoodItem.get().getPhotoKey()).isEqualTo("photo-feijoada-light-123");
        assertThat(updatedFoodItem.get().getAvailable()).isFalse();
        assertThat(updatedFoodItem.get().getDeliveryAvailable()).isFalse();
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldReturnForbiddenWhenUserIsNotOwnerOfRestaurant() throws Exception {
        // 1. Create User 1 (Owner of Restaurant)
        String user1Id = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Dono Um",
                        "dono1@cheffy.com",
                        "dono1",
                        "SenhaSegura@2026",
                        true
                )
        );

        // 2. Create User 2 (Not the owner)
        String user2Id = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Dono Dois",
                        "dono2@cheffy.com",
                        "dono2",
                        "SenhaSegura@2026",
                        true
                )
        );

        // 3. Create Restaurant for User 1
        var restaurantRequest = new RestaurantCreateRequest(
                "Restaurante 1",
                "Brasileira",
                "11.222.333/0001-81",
                "08:00",
                "22:00",
                "America/Sao_Paulo",
                false,
                new RestaurantAddressCreateRequest(
                        "Rua dos Restaurantes",
                        201,
                        "São Paulo",
                        "01001000",
                        "Centro",
                        "SP",
                        "Sala 2"
                )
        );

        String restaurantId = mockMvc.perform(post("/api/v1/restaurants/{userId}", user1Id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 4. Create Food Item in Restaurant
        var foodItemRequest = new FoodItemCreateRequest(
                "Item Teste",
                "Desc Teste",
                new BigDecimal("10.00"),
                "photo-teste",
                true,
                true
        );

        String responseBody = mockMvc.perform(post("/api/v1/restaurants/{restaurantId}/food-items", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodItemRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        FoodItemQueryPort queryPort = objectMapper.readValue(responseBody, FoodItemQueryPort.class);

        // 5. Try to Update Food Item with User 2 (Should Fail)
        var foodItemUpdateRequest = new FoodItemCreateRequest(
                "Item Alterado",
                "Desc Alterado",
                new BigDecimal("20.00"),
                "photo-alterada",
                false,
                false
        );

        mockMvc.perform(patch("/api/v1/restaurants/{restaurantId}/food-items/{foodItemId}", restaurantId, queryPort.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodItemUpdateRequest))
                        .requestAttr("userId", UUID.fromString(user2Id))) // Injecting User 2
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldReturnNotFoundWhenUpdatingNonExistentFoodItem() throws Exception {
         // 1. Create a User
        String userId = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Joao Chef 2",
                        "joao2.chef@cheffy.com",
                        "joao2.chef",
                        "SenhaSegura@2026",
                        true
                )
        );

        // 2. Create a Restaurant
        var restaurantRequest = new RestaurantCreateRequest(
                "Restaurante do Joao 2",
                "Brasileira",
                "11.222.333/0001-81",
                "08:00",
                "22:00",
                "America/Sao_Paulo",
                false,
                new RestaurantAddressCreateRequest(
                        "Rua dos Restaurantes",
                        201,
                        "São Paulo",
                        "01001000",
                        "Centro",
                        "SP",
                        "Sala 2"
                )
        );

        String restaurantId = mockMvc.perform(post("/api/v1/restaurants/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 3. Try to Update a Random UUID
        var foodItemUpdateRequest = new FoodItemCreateRequest(
                "Feijoada Light",
                "Feijoada com carnes magras",
                new BigDecimal("65.00"),
                "photo-feijoada-light-123",
                false,
                false
        );

        mockMvc.perform(patch("/api/v1/restaurants/{restaurantId}/food-items/{foodItemId}", restaurantId, UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodItemUpdateRequest))
                        .requestAttr("userId", UUID.fromString(userId)))
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
    ) {
    }

    private record RestaurantAddressCreateRequest(
            String streetName,
            Integer number,
            String city,
            String postalCode,
            String neighborhood,
            String stateProvince,
            String addressLine
    ) {
    }

    private record FoodItemCreateRequest(
            String name,
            String description,
            BigDecimal price,
            String photoKey,
            boolean deliveryAvailable,
            boolean available
    ) {}
}
