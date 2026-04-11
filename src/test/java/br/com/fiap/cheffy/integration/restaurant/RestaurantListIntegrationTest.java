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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RestaurantListIntegrationTest {

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
    void shouldFindRestaurantByIdSuccessfully() throws Exception {
        // 1. Create User
        String userId = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Dono Encontrado",
                        "dono.find@cheffy.com",
                        "dono.find",
                        "SenhaSegura@2026",
                        true
                )
        );

        // 2. Create Restaurant
        var restaurantRequest = new RestaurantCreateRequest(
                "Restaurante Busca",
                "Hamburgueria",
                "11.222.333/0001-81",
                "10:00",
                "23:00",
                "America/Sao_Paulo",
                false,
                new RestaurantAddressCreateRequest(
                        "Rua da Busca",
                        50,
                        "Belo Horizonte",
                        "03003000",
                        "Centro",
                        "MG",
                        "Loja 1"
                )
        );

        String restaurantIdStr = mockMvc.perform(post("/api/v1/restaurants/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 3. Find Restaurant by ID
        mockMvc.perform(get("/api/v1/restaurants/{id}", restaurantIdStr))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restaurantIdStr))
                .andExpect(jsonPath("$.name").value("Restaurante Busca"))
                .andExpect(jsonPath("$.culinary").value("Hamburgueria"))
                .andExpect(jsonPath("$.address.city").value("Belo Horizonte"));
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldReturnNotFoundWhenRestaurantDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/{id}", UUID.randomUUID()))
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
