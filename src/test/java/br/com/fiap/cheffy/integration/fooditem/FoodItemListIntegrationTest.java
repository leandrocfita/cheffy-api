package br.com.fiap.cheffy.integration.fooditem;

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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FoodItemListIntegrationTest {

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
    void shouldListFoodItemsSuccessfully() throws Exception {
        String userId = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Maria Chef",
                        "maria.chef.list@cheffy.com",
                        "maria.chef.list",
                        "SenhaSegura@2026",
                        true
                )
        );

        var restaurantRequest = new RestaurantCreateRequest(
                "Restaurante Lista",
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

        String restaurantId = mockMvc.perform(post("/api/v1/restaurants/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var foodItem1 = new FoodItemCreateRequest(
                "Macarronada",
                "Massa fresca com molho de tomate",
                new BigDecimal("45.50"),
                "photo-macarronada",
                true,
                true
        );

        var foodItem2 = new FoodItemCreateRequest(
                "Lasanha",
                "Massa fresca com molho bolonhesa",
                new BigDecimal("55.00"),
                "photo-lasanha",
                true,
                true
        );

        mockMvc.perform(post("/api/v1/restaurants/{restaurantId}/food-items", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodItem1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/restaurants/{restaurantId}/food-items", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodItem2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/restaurants/{restaurantId}/food-items", restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("Lasanha")) // Default sort is name ASC
                .andExpect(jsonPath("$.content[1].name").value("Macarronada"));
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldReturnEmptyListWhenRestaurantHasNoFoodItems() throws Exception {
         String userId = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Maria Chef Empty",
                        "maria.chef.empty@cheffy.com",
                        "maria.chef.empty",
                        "SenhaSegura@2026",
                        true
                )
        );

        var restaurantRequest = new RestaurantCreateRequest(
                "Restaurante Vazio",
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

        String restaurantId = mockMvc.perform(post("/api/v1/restaurants/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        mockMvc.perform(get("/api/v1/restaurants/{restaurantId}/food-items", restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldReturnNotFoundWhenListingFoodItemsForNonExistentRestaurant() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/{restaurantId}/food-items", UUID.randomUUID()))
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

    private record FoodItemCreateRequest(
            String name,
            String description,
            BigDecimal price,
            String photoKey,
            boolean deliveryAvailable,
            boolean available
    ) {}
}