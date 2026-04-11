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
public class FoodItemCreationIntegrationTest {

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
    void shouldCreateFoodItemFromHttpRequestAndPersistInDatabase() throws Exception {
        // 1. Create a User
        String userId = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Maria Chef",
                        "maria.chef@cheffy.com",
                        "maria.chef",
                        "SenhaSegura@2026",
                        true
                )
        );

        // 2. Create a Restaurant for the User
        var restaurantRequest = new RestaurantCreateRequest(
                "Restaurante da Maria",
                "Italiana",
                "11.222.333/0001-81",
                "08:00",
                "22:00",
                "America/Sao_Paulo",
                false,
                new RestaurantAddressCreateRequest(
                        "Rua dos Restaurantes",
                        200,
                        "São Paulo",
                        "01001000",
                        "Centro",
                        "SP",
                        "Sala 1"
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
                "Macarronada",
                "Massa fresca com molho de tomate",
                new BigDecimal("45.50"),
                "photo-macarronada-123",
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
        
        assertThat(queryPort).isNotNull();
        assertThat(queryPort.id()).isNotNull();

        // 4. Verify in Database
        var savedFoodItem = foodItemJpaRepository.findById(queryPort.id());
        assertThat(savedFoodItem).isPresent();
        assertThat(savedFoodItem.get().getName()).isEqualTo("Macarronada");
        assertThat(savedFoodItem.get().getDescription()).isEqualTo("Massa fresca com molho de tomate");
        assertThat(savedFoodItem.get().getPrice()).isEqualByComparingTo("45.50");
        assertThat(savedFoodItem.get().getRestaurant().getId()).isEqualTo(UUID.fromString(createdRestaurantId));
        assertThat(savedFoodItem.get().getAvailable()).isTrue();
        assertThat(savedFoodItem.get().getDeliveryAvailable()).isTrue();
        assertThat(savedFoodItem.get().getActive()).isTrue();
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldReturnBadRequestWhenFoodItemNameIsBlank() throws Exception {
        // 1. Create a User
        String userId = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Jose Chef",
                        "joao.chef@cheffy.com",
                        "joao.chef",
                        "SenhaSegura@2026",
                        true
                )
        );

        // 2. Create a Restaurant for the User
        var restaurantRequest = new RestaurantCreateRequest(
                "Restaurante do Jose",
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

        // 3. Try to create a Food Item with blank name
        var foodItemRequest = new FoodItemCreateRequest(
                "", // Blank name
                "Feijoada completa",
                new BigDecimal("60.00"),
                "photo-feijoada-123",
                true,
                true
        );

        mockMvc.perform(post("/api/v1/restaurants/{restaurantId}/food-items", createdRestaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodItemRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldReturnNotFoundWhenCreatingFoodItemForNonExistentRestaurant() throws Exception {
        // Try to create a Food Item for a random UUID restaurant
        var foodItemRequest = new FoodItemCreateRequest(
                "Macarronada",
                "Massa fresca com molho de tomate",
                new BigDecimal("45.50"),
                "photo-macarronada-123",
                true,
                true
        );

        mockMvc.perform(post("/api/v1/restaurants/{restaurantId}/food-items", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodItemRequest)))
                .andExpect(status().isNotFound());
    }

    public record RestaurantCreateRequest(
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

    public record RestaurantAddressCreateRequest(
            String streetName,
            Integer number,
            String city,
            String postalCode,
            String neighborhood,
            String stateProvince,
            String addressLine
    ) {
    }

    public record FoodItemCreateRequest(
            String name,
            String description,
            BigDecimal price,
            String photoKey,
            boolean deliveryAvailable,
            boolean available
    ) {}
}
