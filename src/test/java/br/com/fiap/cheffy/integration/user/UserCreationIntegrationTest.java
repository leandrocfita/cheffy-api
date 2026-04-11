package br.com.fiap.cheffy.integration.user;

import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.infrastructure.persistence.profile.entity.ProfileJpaEntity;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserCreationIntegrationTest {

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        restaurantJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
        IntegrationTestUserHelper.ensureProfile(profileJpaRepository, ProfileType.CLIENT);
    }

    @Test
    void shouldCreateUserFromHttpRequestAndPersistInDatabase() throws Exception {
        var request = IntegrationTestUserHelper.buildUserCreateRequest(
                "Maria Silva",
                "maria.silva@cheffy.com",
                "maria.silva",
                "SenhaSegura@2026",
                true
        );

        String createdId = IntegrationTestUserHelper.createUserAndReturnId(mockMvc, objectMapper, request);

        assertThat(createdId).isNotBlank();

        var savedUser = userJpaRepository.findByLogin("maria.silva");
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getEmail()).isEqualTo("maria.silva@cheffy.com");
        assertThat(savedUser.get().getName()).isEqualTo("Maria Silva");
        assertThat(savedUser.get().getPassword()).isNotEqualTo("SenhaSegura@2026");
        assertThat(passwordEncoder.matches("SenhaSegura@2026", savedUser.get().getPassword())).isTrue();
        assertThat(savedUser.get().getProfiles())
                .extracting(ProfileJpaEntity::getType)
                .containsExactly(ProfileType.CLIENT.name());
        assertThat(savedUser.get().getAddresses()).hasSize(1);
        assertThat(savedUser.get().isActive()).isTrue();
    }

    @Test
    void shouldRejectCreationWhenFirstAddressIsNotMain() throws Exception {
        var request = IntegrationTestUserHelper.buildUserCreateRequest(
                "Maria Silva",
                "maria.regra@cheffy.com",
                "maria.regra",
                "SenhaSegura@2026",
                false
        );

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        assertThat(userJpaRepository.findByLogin("maria.regra")).isEmpty();
    }

}
