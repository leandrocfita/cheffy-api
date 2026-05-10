//package br.com.fiap.cheffy.integration.user;
//
//import br.com.fiap.cheffy.application.user.dto.UserCommandPort;
//import br.com.fiap.cheffy.domain.profile.ProfileType;
//import br.com.fiap.cheffy.domain.user.entity.AuthStatus;
//import br.com.fiap.cheffy.domain.user.port.output.AuthUserExternalClient;
//import br.com.fiap.cheffy.infrastructure.persistence.profile.repository.ProfileJpaRepository;
//import br.com.fiap.cheffy.infrastructure.persistence.user.repository.UserJpaRepository;
//import br.com.fiap.cheffy.integration.helper.IntegrationTestUserHelper;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//class UserCreationIntegrationTest {
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
//    private AuthUserExternalClient authUserExternalClient;
//
//    @BeforeEach
//    void setUp() {
//        userJpaRepository.deleteAll();
//        IntegrationTestUserHelper.ensureProfile(profileJpaRepository, ProfileType.CLIENT);
//    }
//
//    @Test
//    @DisplayName("Should create user and address when request is valid and auth service succeeds")
//    void shouldCreateUserFromHttpRequestAndPersistInDatabase() throws Exception {
//        // Arrange
//        var request = IntegrationTestUserHelper.buildUserCreateRequest(
//                "Maria Silva", "maria.silva@cheffy.com", "maria.silva", "SenhaSegura@2026", true
//        );
//
//        when(authUserExternalClient.createUser(any())).thenReturn("fake-auth-id-123");
//
//        // Act
//        mockMvc.perform(post("/api/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated());
//
//        // Assert
//        var savedUserOpt = userJpaRepository.findByEmail("maria.silva@cheffy.com");
//        assertThat(savedUserOpt).isPresent();
//
//        var savedUser = savedUserOpt.get();
//        assertThat(savedUser.getName()).isEqualTo("Maria Silva");
//        assertThat(savedUser.getAuthId()).isEqualTo("fake-auth-id-123");
//        assertThat(savedUser.getAuthStatus()).isEqualTo(AuthStatus.CONFIRMED);
//        assertThat(savedUser.getProfiles()).hasSize(1);
//        assertThat(savedUser.getProfiles().iterator().next().getType()).isEqualTo(ProfileType.CLIENT.name());
//        assertThat(savedUser.getAddresses()).hasSize(1);
//        assertThat(savedUser.getAddresses().stream().toList().getFirst().isMain()).isTrue();
//    }
//
//    @Test
//    @DisplayName("Should return conflict when first address is not the main one")
//    void shouldRejectCreationWhenFirstAddressIsNotMain() throws Exception {
//        // Arrange
//        var request = IntegrationTestUserHelper.buildUserCreateRequest(
//                "Maria Regra", "maria.regra@cheffy.com", "maria.regra", "SenhaSegura@2026", false
//        );
//
//        // Act & Assert
//        mockMvc.perform(post("/api/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isConflict());
//
//        assertThat(userJpaRepository.findByEmail("maria.regra@cheffy.com")).isEmpty();
//    }
//
//    @Test
//    @DisplayName("Should return conflict when user email already exists and is confirmed")
//    void shouldReturnConflictWhenUserIsAlreadyConfirmed() throws Exception {
//        // Arrange
//        // 1. Create an already existing and confirmed user
//        var existingUser = IntegrationTestUserHelper.createAndSaveUser(
//                userJpaRepository, "Existing User", "existing.user@cheffy.com", "existing-auth-id"
//        );
//        existingUser.finishAuthIntegration("existing-auth-id");
//        userJpaRepository.save(existingUser);
//
//        // 2. Prepare a new request with the same email
//        var request = IntegrationTestUserHelper.buildUserCreateRequest(
//                "New User", "existing.user@cheffy.com", "new.login", "NewPassword@123", true
//        );
//
//        // Act & Assert
//        mockMvc.perform(post("/api/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isConflict());
//    }
//}
