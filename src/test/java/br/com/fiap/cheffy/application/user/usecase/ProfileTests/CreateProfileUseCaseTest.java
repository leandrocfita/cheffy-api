package br.com.fiap.cheffy.application.user.usecase.ProfileTests;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputPort;
import br.com.fiap.cheffy.application.profile.usecase.CreateProfileUseCase;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.exception.ProfileAlreadyExistException;
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
public class CreateProfileUseCaseTest {

    @Mock
    private ProfileRepository profileGateway;

    @InjectMocks
    private CreateProfileUseCase profileCreateUseCase;

    @Test
    @DisplayName("Should create profile successfully when input is valid")
    void shouldCreateProfileSuccessfully() {
        // Given
        String profileType = "Chef";
        ProfileInputPort inputDto = new ProfileInputPort(profileType);

        // Mock findByType to return empty (no duplicate found)
        when(profileGateway.findByType(profileType)).thenReturn(Optional.empty());
        
        // Mock save to return the ID
        when(profileGateway.save(any(Profile.class))).thenReturn(1L);

        // When
        Long resultId = profileCreateUseCase.create(inputDto);

        // Then
        assertNotNull(resultId);
        assertEquals(1L, resultId);
        verify(profileGateway, times(1)).findByType(profileType);
        verify(profileGateway, times(1)).save(any(Profile.class));
    }

    @Test
    @DisplayName("Should throw ProfileAlreadyExistException when profile already exists")
    void shouldThrowExceptionWhenProfileAlreadyExists() {
        // Given
        String profileType = "Chef";
        ProfileInputPort inputDto = new ProfileInputPort(profileType);
        Profile existingProfile = Profile.create(1L, profileType);

        // Mock findByType to return an existing profile
        when(profileGateway.findByType(profileType)).thenReturn(Optional.of(existingProfile));

        // When & Then
        assertThrows(ProfileAlreadyExistException.class, () -> profileCreateUseCase.create(inputDto));
        verify(profileGateway, times(1)).findByType(profileType);
        verify(profileGateway, never()).save(any(Profile.class));
    }

    @Test
    @DisplayName("Should throw exception when gateway fails")
    void shouldThrowExceptionWhenGatewayFails() {
        String profileType = "Chef";
        ProfileInputPort inputDto = new ProfileInputPort(profileType);

        // Mock findByType to pass
        when(profileGateway.findByType(profileType)).thenReturn(Optional.empty());
        // Mock save to fail
        when(profileGateway.save(any(Profile.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> profileCreateUseCase.create(inputDto));
    }
}
