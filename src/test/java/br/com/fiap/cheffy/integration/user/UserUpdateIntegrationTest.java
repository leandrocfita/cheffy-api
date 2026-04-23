//package br.com.fiap.cheffy.integration.user;
//
//import br.com.fiap.cheffy.domain.profile.ProfileType;
//import br.com.fiap.cheffy.infrastructure.persistence.profile.repository.ProfileJpaRepository;
//import br.com.fiap.cheffy.infrastructure.persistence.restaurant.repository.RestaurantJpaRepository;
//import br.com.fiap.cheffy.infrastructure.persistence.user.repository.UserJpaRepository;
//import br.com.fiap.cheffy.integration.helper.IntegrationTestUserHelper;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//public class UserUpdateIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private UserJpaRepository userJpaRepository;
//
//    @Autowired
//    private ProfileJpaRepository profileJpaRepository;
//
//    @Autowired
//    private RestaurantJpaRepository restaurantJpaRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @BeforeEach
//    void setUp() {
//        restaurantJpaRepository.deleteAll();
//        userJpaRepository.deleteAll();
//        IntegrationTestUserHelper.ensureProfile(profileJpaRepository, ProfileType.CLIENT);
//    }
//
//    @Test
//    @WithMockUser(username = "integration-user")
//    void shouldUpdateUserDetailsSuccessfully() throws Exception {
//        // 1. Create a user
//        var createRequest = IntegrationTestUserHelper.buildUserCreateRequest(
//                "Carlos Silva",
//                "carlos.silva@cheffy.com",
//                "carlos.silva",
//                "SenhaSegura@2026",
//                true
//        );
//
//        String userIdStr = IntegrationTestUserHelper.createUserAndReturnId(mockMvc, objectMapper, createRequest);
//        UUID userId = UUID.fromString(userIdStr);
//
//        // 2. Build Update Request
//        var updateRequest = new UserUpdateDTO(
//                "Carlos Silva Atualizado",
//                "carlos.atualizado@cheffy.com",
//                "carlos.silva" // login usually shouldn't change or is handled elsewhere
//        );
//
//        // 3. Perform Update Request
//        mockMvc.perform(patch("/api/v1/users/{id}", userId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateRequest)))
//                .andExpect(status().isNoContent());
//
//        // 4. Verify in DB
//        var updatedUser = userJpaRepository.findById(userId);
//        assertThat(updatedUser).isPresent();
//        assertThat(updatedUser.get().getName()).isEqualTo("Carlos Silva Atualizado");
//        assertThat(updatedUser.get().getEmail()).isEqualTo("carlos.atualizado@cheffy.com");
//    }
//
//    @Test
//    @WithMockUser(username = "integration-user")
//    void shouldReturnNotFoundWhenUpdatingNonExistentUser() throws Exception {
//        var updateRequest = new UserUpdateDTO(
//                "Fantasma",
//                "fantasma@cheffy.com",
//                "fantasma"
//        );
//
//        mockMvc.perform(patch("/api/v1/users/{id}", UUID.randomUUID())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateRequest)))
//                .andExpect(status().isNotFound());
//    }
//
//    private record UserUpdateDTO(
//            String name,
//            String email,
//            String login
//    ) {}
//
//}