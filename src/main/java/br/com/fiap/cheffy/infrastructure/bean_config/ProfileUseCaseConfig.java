package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.profile.mapper.ProfileQueryMapper;
import br.com.fiap.cheffy.application.profile.service.ProfileServiceHelper;
import br.com.fiap.cheffy.application.profile.usecase.*;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProfileUseCaseConfig {


    @Bean
    CreateProfileUseCase createProfileUseCase(ProfileRepository profileRepository) {

        return new CreateProfileUseCase(profileRepository);
    }

    @Bean
    public ListAllProfilesUseCase listAllProfilesUseCase(
            ProfileRepository profileRepository,
            ProfileQueryMapper mapper) {
        return new ListAllProfilesUseCase(profileRepository, mapper);
    }

    @Bean
    UpdateProfileUseCase updateProfileUseCase(
            ProfileRepository profileRepository,
            ProfileServiceHelper profileServiceHelper) {
        return new UpdateProfileUseCase(profileRepository, profileServiceHelper);
    }

    @Bean
    public FindProfileByIdUseCase findProfileByIdUseCase(
            ProfileRepository profileRepository,
            ProfileQueryMapper mapper) {
        return new FindProfileByIdUseCase(profileRepository, mapper);
    }

    @Bean
    public ProfileServiceHelper profileServiceHelper(
            ProfileRepository profileRepository) {
        return new ProfileServiceHelper(profileRepository);
    }

    @Bean
    public DeleteProfileUseCase deleteProfileUseCase(
            ProfileServiceHelper profileServiceHelper,
            ProfileRepository profileRepository) {
        return new DeleteProfileUseCase(profileServiceHelper, profileRepository);
    }
}
