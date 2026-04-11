package br.com.fiap.cheffy.application.profile.usecase;

import br.com.fiap.cheffy.application.profile.service.ProfileServiceHelper;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.exception.ProfileIsOwnerOrClientException;
import br.com.fiap.cheffy.domain.profile.exception.ProfileNotFoundException;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteProfileUseCaseTest {

    @Mock
    private ProfileServiceHelper profileServiceHelper;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private DeleteProfileUseCase useCase;

    @Test
    @DisplayName("Should delete profile when id exists")
    void shouldDeleteWhenIdExists() {
        Long id = 1L;
        Profile profile = Profile.create(id, "ADMIN");

        when(profileServiceHelper.getProfileOrFail(id)).thenReturn(profile);
        when(profileServiceHelper.validateProfileModification(profile)).thenReturn(profile);
        doNothing().when(profileRepository).delete(any(Profile.class));

        useCase.execute(id);

        verify(profileServiceHelper, times(1)).getProfileOrFail(id);
        verify(profileRepository, times(1)).delete(profile);
    }

    @Test
    @DisplayName("Should throw exception when profile id does not exist")
    void shouldThrowWhenIdDoesNotExist() {
        Long id = 999L;

        when(profileServiceHelper.getProfileOrFail(id))
                .thenThrow(new ProfileNotFoundException(ExceptionsKeys.PROFILE_NOT_FOUND_EXCEPTION, id.toString()));

        assertThrows(ProfileNotFoundException.class, () -> useCase.execute(id));

        verify(profileServiceHelper, times(1)).getProfileOrFail(id);
        verify(profileRepository, never()).delete(any(Profile.class));
    }

    @Test
    @DisplayName("Should throw exception when trying to delete CLIENT profile")
    void shouldThrowWhenDeletingClientProfile() {
        Long id = 2L;
        Profile client = Profile.create(id, ProfileType.CLIENT.name());

        when(profileServiceHelper.getProfileOrFail(id))
                .thenThrow(new ProfileIsOwnerOrClientException(ExceptionsKeys.PROFILE_IS_OWNER_OR_CLIENT));

        assertThrows(ProfileIsOwnerOrClientException.class, () -> useCase.execute(id));

        verify(profileServiceHelper, times(1)).getProfileOrFail(id);
        verify(profileRepository, never()).delete(client);
        verify(profileRepository, never()).delete(any(Profile.class));
    }

    @Test
    @DisplayName("Should throw exception when trying to delete OWNER profile")
    void shouldThrowWhenDeletingOwnerProfile() {
        Long id = 3L;
        Profile owner = Profile.create(id, ProfileType.OWNER.name());

        when(profileServiceHelper.getProfileOrFail(id))
                .thenThrow(new ProfileIsOwnerOrClientException(ExceptionsKeys.PROFILE_IS_OWNER_OR_CLIENT));

        assertThrows(ProfileIsOwnerOrClientException.class, () -> useCase.execute(id));

        verify(profileServiceHelper, times(1)).getProfileOrFail(id);
        verify(profileRepository, never()).delete(owner);
        verify(profileRepository, never()).delete(any(Profile.class));
    }
}