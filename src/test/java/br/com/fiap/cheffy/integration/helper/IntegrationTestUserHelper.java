package br.com.fiap.cheffy.integration.helper;

import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.infrastructure.persistence.profile.entity.ProfileJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.profile.repository.ProfileJpaRepository;
import br.com.fiap.cheffy.presentation.dto.AddressCreateDTO;
import br.com.fiap.cheffy.presentation.dto.UserCreateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public final class IntegrationTestUserHelper {

    private IntegrationTestUserHelper() {
    }

    public static void ensureProfile(ProfileJpaRepository profileJpaRepository, ProfileType profileType) {
        profileJpaRepository.findByType(profileType.name())
                .orElseGet(() -> {
                    ProfileJpaEntity profile = new ProfileJpaEntity();
                    profile.setType(profileType.name());
                    return profileJpaRepository.save(profile);
                });
    }

    public static String createUserAndReturnId(MockMvc mockMvc,
                                               ObjectMapper objectMapper,
                                               UserCreateDTO request) throws Exception {
        return mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    public static UserCreateDTO buildUserCreateRequest(String name,
                                                       String email,
                                                       String login,
                                                       String password,
                                                       boolean mainAddress) {
        return new UserCreateDTO(
                name,
                email,
                login,
                password,
                new AddressCreateDTO(
                        "Rua das Flores",
                        100,
                        "São Paulo",
                        "01001000",
                        "Centro",
                        "SP",
                        "Apto 12",
                        mainAddress
                )
        );
    }
}
