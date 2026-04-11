package br.com.fiap.cheffy.application.profile.service;

import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.exception.ProfileIsOwnerOrClientException;
import br.com.fiap.cheffy.domain.profile.exception.ProfileNotFoundException;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.PROFILE_IS_OWNER_OR_CLIENT;
import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.PROFILE_NOT_FOUND_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProfileServiceHelperTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileServiceHelper profileServiceHelper;

    @Test
    @DisplayName("Should return profile when id exists and profile is not OWNER or CLIENT")
    void shouldReturnProfileWhenIdExistsAndProfileIsNotDefault() {
        Long id = 1L;
        Profile profile = Profile.create(id, "ADMIN");

        when(profileRepository.findById(id)).thenReturn(Optional.of(profile));

        Profile result = profileServiceHelper.getProfileOrFail(id);

        assertNotNull(result);
        assertEquals(profile, result);
        verify(profileRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw ProfileNotFoundException when profile id does not exist")
    void shouldThrowProfileNotFoundExceptionWhenIdDoesNotExist() {
        Long id = 999L;

        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        ProfileNotFoundException exception = assertThrows(
                ProfileNotFoundException.class,
                () -> profileServiceHelper.getProfileOrFail(id)
        );

        assertEquals(PROFILE_NOT_FOUND_EXCEPTION.toString(), exception.getMessage());
        assertEquals(id.toString(), exception.getType());
        verify(profileRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw ProfileIsOwnerOrClientException when profile type is CLIENT")
    void shouldThrowProfileIsOwnerOrClientExceptionWhenProfileIsClient() {
        Long id = 2L;
        Profile clientProfile = Profile.create(id, ProfileType.CLIENT.name());

        ProfileIsOwnerOrClientException exception = assertThrows(
                ProfileIsOwnerOrClientException.class,
                () -> profileServiceHelper.validateProfileModification(clientProfile)
        );

        assertEquals(PROFILE_IS_OWNER_OR_CLIENT.toString(), exception.getMessage());
    }

    @Test
    @DisplayName("Should throw ProfileIsOwnerOrClientException when profile type is OWNER")
    void shouldThrowProfileIsOwnerOrClientExceptionWhenProfileIsOwner() {
        Long id = 3L;
        Profile ownerProfile = Profile.create(id, ProfileType.OWNER.name());

        ProfileIsOwnerOrClientException exception = assertThrows(
                ProfileIsOwnerOrClientException.class,
                () -> profileServiceHelper.validateProfileModification(ownerProfile)
        );

        assertEquals(PROFILE_IS_OWNER_OR_CLIENT.toString(), exception.getMessage());
    }
}