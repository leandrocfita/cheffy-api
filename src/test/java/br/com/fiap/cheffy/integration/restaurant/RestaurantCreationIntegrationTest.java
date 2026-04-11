package br.com.fiap.cheffy.integration.restaurant;

import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.infrastructure.persistence.profile.entity.ProfileJpaEntity;
import br.com.fiap.cheffy.integration.helper.IntegrationTestUserHelper;
import br.com.fiap.cheffy.infrastructure.persistence.profile.repository.ProfileJpaRepository;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.repository.RestaurantJpaRepository;
import br.com.fiap.cheffy.infrastructure.persistence.user.repository.UserJpaRepository;
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

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RestaurantCreationIntegrationTest {

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
    void shouldCreateRestaurantFromHttpRequestAndPersistInDatabase() throws Exception {
        String userId = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Maria Silva",
                        "maria.owner@cheffy.com",
                        "maria.owner",
                        "SenhaSegura@2026",
                        true
                )
        );

        var request = new RestaurantCreateRequest(
                "Cantina da Maria",
                "Italiana",
                "27865757000102",
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
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UUID restaurantId = UUID.fromString(createdRestaurantId);

        var savedRestaurant = restaurantJpaRepository.findById(restaurantId);
        assertThat(savedRestaurant).isPresent();
        assertThat(savedRestaurant.get().getName()).isEqualTo("Cantina da Maria");
        assertThat(savedRestaurant.get().getCnpj()).isEqualTo("27865757000102");
        assertThat(savedRestaurant.get().getUser().getId()).isEqualTo(UUID.fromString(userId));
        assertThat(savedRestaurant.get().getAddress()).isNotNull();
        assertThat(savedRestaurant.get().getAddress().getMain()).isTrue();
        assertThat(savedRestaurant.get().getActive()).isTrue();

        var savedUser = userJpaRepository.findById(UUID.fromString(userId));
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getProfiles())
                .extracting(ProfileJpaEntity::getType)
                .containsAll(Set.of(ProfileType.CLIENT.name(), ProfileType.OWNER.name()));
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
}

