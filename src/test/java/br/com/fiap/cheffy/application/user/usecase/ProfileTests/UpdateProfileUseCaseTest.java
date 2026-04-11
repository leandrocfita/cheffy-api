package br.com.fiap.cheffy.application.user.usecase.ProfileTests;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputPort;
import br.com.fiap.cheffy.application.profile.service.ProfileServiceHelper;
import br.com.fiap.cheffy.application.profile.usecase.UpdateProfileUseCase;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.exception.ProfileNotFoundException;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateProfileUseCaseTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileServiceHelper profileServiceHelper;

    @InjectMocks
    private UpdateProfileUseCase updateProfileUseCase;

    @Test
    @DisplayName("Should update profile by ID successfully when profile exists")
    void shouldUpdateProfileByIdSuccessfully() {
        // Given
        Long profileId = 1L;
        String newProfileType = "Chef";
        ProfileInputPort inputDto = new ProfileInputPort(newProfileType);
        Profile existingProfile = Profile.create(profileId, "Client");

        // Mock findById to return the existing profile
        when(profileServiceHelper.getProfileOrFail(profileId)).thenReturn(existingProfile);

        when(profileServiceHelper.validateProfileModification(existingProfile)).thenReturn(existingProfile);
        
        // Mock save
        when(profileRepository.save(any(Profile.class))).thenReturn(profileId);

        // When
        updateProfileUseCase.updateById(profileId, inputDto);

        // Then
        assertEquals(newProfileType, existingProfile.getType());
        verify(profileRepository, times(1)).save(existingProfile);
    }

    @Test
    @DisplayName("Should throw ProfileNotFoundException when updating by ID and profile does not exist")
    void shouldThrowExceptionWhenProfileByIdNotFound() {
        // Given
        Long profileId = 1L;
        String newProfileType = "Chef";
        ProfileInputPort inputDto = new ProfileInputPort(newProfileType);

        // Mock findById to return empty
        when(profileServiceHelper.getProfileOrFail(profileId)).thenThrow(ProfileNotFoundException.class);

        // When & Then
        assertThrows(ProfileNotFoundException.class, () -> updateProfileUseCase.updateById(profileId, inputDto));
        verify(profileRepository, never()).save(any(Profile.class));
    }

    @Test
    @DisplayName("Should update profile by Name successfully when profile exists")
    void shouldUpdateProfileByNameSuccessfully() {
        // Given
        String currentProfileType = "Client";
        String newProfileType = "Chef";
        ProfileInputPort inputDto = new ProfileInputPort(newProfileType);
        Profile existingProfile = Profile.create(1L, currentProfileType);

        // Mock findByType to return the existing profile
        when(profileRepository.findByType(currentProfileType)).thenReturn(Optional.of(existingProfile));
        
        // Mock save
        when(profileRepository.save(any(Profile.class))).thenReturn(1L);

        // When
        updateProfileUseCase.updateByName(currentProfileType, inputDto);

        // Then
        assertEquals(newProfileType, existingProfile.getType());
        verify(profileRepository, times(1)).findByType(currentProfileType);
        verify(profileRepository, times(1)).save(existingProfile);
    }

    @Test
    @DisplayName("Should throw ProfileNotFoundException when updating by Name and profile does not exist")
    void shouldThrowExceptionWhenProfileByNameNotFound() {
        // Given
        String currentProfileType = "Client";
        String newProfileType = "Chef";
        ProfileInputPort inputDto = new ProfileInputPort(newProfileType);

        // Mock findByType to return empty
        when(profileRepository.findByType(currentProfileType)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ProfileNotFoundException.class, () -> updateProfileUseCase.updateByName(currentProfileType, inputDto));
        verify(profileRepository, times(1)).findByType(currentProfileType);
        verify(profileRepository, never()).save(any(Profile.class));
    }
}
