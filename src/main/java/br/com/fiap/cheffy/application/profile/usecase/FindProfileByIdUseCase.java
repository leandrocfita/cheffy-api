package br.com.fiap.cheffy.application.profile.usecase;

import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import br.com.fiap.cheffy.application.profile.mapper.ProfileQueryMapper;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.exception.ProfileNotFoundException;
import br.com.fiap.cheffy.domain.profile.port.input.FindProfileByInput;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class FindProfileByIdUseCase implements FindProfileByInput {

    private final ProfileRepository profileRepository;
    private final ProfileQueryMapper mapper;

    public FindProfileByIdUseCase(ProfileRepository profileRepository, ProfileQueryMapper mapper) {
        this.profileRepository = profileRepository;
        this.mapper = mapper;
    }

    @Override
    public ProfileQueryPort execute(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException(ExceptionsKeys.PROFILE_NOT_FOUND_EXCEPTION, id.toString()));

        return mapper.toQuery(profile);
    }
}
