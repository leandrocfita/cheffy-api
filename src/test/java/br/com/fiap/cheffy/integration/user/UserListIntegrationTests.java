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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserListIntegrationTests {

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
    void shouldListAllUsersSuccessfully() throws Exception {
        // Create multiple users
        IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc, objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest("Alice Souza", "alice@cheffy.com", "alice", "Senha@123564782", true)
        );

        IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc, objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest("Bruno Silva", "bruno@cheffy.com", "bruno", "Senha@123727126", true)
        );

        mockMvc.perform(get("/api/v1/users")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("direction", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("Alice Souza"))
                .andExpect(jsonPath("$.content[1].name").value("Bruno Silva"));
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldFindUserByIdSuccessfully() throws Exception {
        String userIdStr = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc, objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest("Carlos Mendes", "carlos@cheffy.com", "carlos", "Senha@123412313", true)
        );

        mockMvc.perform(get("/api/v1/users/{id}", userIdStr))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Carlos Mendes"))
                .andExpect(jsonPath("$.email").value("carlos@cheffy.com"));
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldReturnNotFoundWhenFindingNonExistentUserById() throws Exception {
        mockMvc.perform(get("/api/v1/users/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldSearchUsersByNameSuccessfully() throws Exception {
        IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc, objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest("Daniel Oliveira", "daniel@cheffy.com", "daniel", "Senha@12321212", true)
        );

        IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc, objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest("Daniela Costa", "daniela@cheffy.com", "daniela", "Senha@123121212", true)
        );

        IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc, objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest("Eduardo Lima", "eduardo@cheffy.com", "eduardo", "Senha@1232121", true)
        );

        mockMvc.perform(get("/api/v1/users")
                        .param("name", "Daniel")
                        .param("sortBy", "name")
                        .param("direction", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("Daniel Oliveira"))
                .andExpect(jsonPath("$.content[1].name").value("Daniela Costa"));
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldReturnEmptyPageWhenSearchingUserByNonExistentName() throws Exception {
        IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc, objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest("Fernando Dias", "fernando@cheffy.com", "fernando", "Senha@123212312", true)
        );

        mockMvc.perform(get("/api/v1/users")
                        .param("name", "Zebra"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0));
    }
}
