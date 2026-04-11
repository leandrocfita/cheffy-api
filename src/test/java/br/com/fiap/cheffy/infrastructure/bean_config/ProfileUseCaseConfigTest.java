package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.profile.mapper.ProfileQueryMapper;
import br.com.fiap.cheffy.application.profile.service.ProfileServiceHelper;
import br.com.fiap.cheffy.application.profile.usecase.*;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProfileUseCaseConfigTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileQueryMapper profileQueryMapper;

    @Mock
    private ProfileServiceHelper profileServiceHelper;

    @Test
    void createProfileUseCaseCreatesBean() {
        ProfileUseCaseConfig config = new ProfileUseCaseConfig();

        CreateProfileUseCase useCase = config.createProfileUseCase(profileRepository);

        assertThat(useCase).isNotNull();
    }

    @Test
    void listAllProfilesUseCaseCreatesBean() {
        ProfileUseCaseConfig config = new ProfileUseCaseConfig();

        ListAllProfilesUseCase useCase = config.listAllProfilesUseCase(profileRepository, profileQueryMapper);

        assertThat(useCase).isNotNull();
    }

    @Test
    void updateProfileUseCaseCreatesBean() {
        ProfileUseCaseConfig config = new ProfileUseCaseConfig();

        UpdateProfileUseCase useCase = config.updateProfileUseCase(profileRepository, profileServiceHelper);

        assertThat(useCase).isNotNull();
    }

    @Test
    void findProfileByIdUseCaseCreatesBean() {
        ProfileUseCaseConfig config = new ProfileUseCaseConfig();

        FindProfileByIdUseCase useCase = config.findProfileByIdUseCase(profileRepository, profileQueryMapper);

        assertThat(useCase).isNotNull();
    }

    @Test
    void profileServiceHelperCreatesBean() {
        ProfileUseCaseConfig config = new ProfileUseCaseConfig();

        ProfileServiceHelper helper = config.profileServiceHelper(profileRepository);

        assertThat(helper).isNotNull();
    }

    @Test
    void deleteProfileUseCaseCreatesBean() {
        ProfileUseCaseConfig config = new ProfileUseCaseConfig();

        DeleteProfileUseCase useCase = config.deleteProfileUseCase(profileServiceHelper, profileRepository);

        assertThat(useCase).isNotNull();
    }

}