package br.com.fiap.cheffy.application.profile.usecase;

import br.com.fiap.cheffy.application.profile.service.ProfileServiceHelper;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.port.input.ProfileDeleteInput;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;

public class DeleteProfileUseCase implements ProfileDeleteInput {

    private final ProfileServiceHelper profileServiceHelper;
    private final ProfileRepository profileRepository;

    public DeleteProfileUseCase(ProfileServiceHelper profileServiceHelper,
                                ProfileRepository profileRepository) {
        this.profileServiceHelper = profileServiceHelper;
        this.profileRepository = profileRepository;
    }

    @Override
    public void execute(Long id) {

        Profile profile = profileServiceHelper.getProfileOrFail(id);

        profileRepository.delete(
                profileServiceHelper.validateProfileModification(profile));
    }
}
