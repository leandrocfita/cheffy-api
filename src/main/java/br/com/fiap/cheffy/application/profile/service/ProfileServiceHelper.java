package br.com.fiap.cheffy.application.profile.service;

import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.exception.ProfileIsOwnerOrClientException;
import br.com.fiap.cheffy.domain.profile.exception.ProfileNotFoundException;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;

import java.util.Optional;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.PROFILE_IS_OWNER_OR_CLIENT;
import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.PROFILE_NOT_FOUND_EXCEPTION;

public class ProfileServiceHelper {

    private final ProfileRepository profileRepository;

    public ProfileServiceHelper(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile validateProfileModification(Profile profile) {
        return thorwExceptionCaseProfileIsOwnerOrClient(profile);
    }

    public Profile getProfileOrFail(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException(PROFILE_NOT_FOUND_EXCEPTION, id.toString()));
    }

    private Profile thorwExceptionCaseProfileIsOwnerOrClient(Profile profile) {
        return Optional.ofNullable(profile)
                .filter(p -> !p.getType().equalsIgnoreCase(ProfileType.OWNER.name())
                        && !p.getType().equalsIgnoreCase(ProfileType.CLIENT.name()))
                .orElseThrow(() -> new ProfileIsOwnerOrClientException(PROFILE_IS_OWNER_OR_CLIENT));
    }
}
