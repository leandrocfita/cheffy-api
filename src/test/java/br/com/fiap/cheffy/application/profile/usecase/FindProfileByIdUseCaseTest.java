package br.com.fiap.cheffy.application.profile.usecase;

import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import br.com.fiap.cheffy.application.profile.mapper.ProfileQueryMapper;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.exception.ProfileNotFoundException;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindProfileByIdUseCaseTest {

    @Mock
    private ProfileRepository repository;

    @Mock
    ProfileQueryMapper mapper;

    @InjectMocks
    private FindProfileByIdUseCase useCase;

    private Profile profile;
    private ProfileQueryPort mappedProfile;

    @BeforeEach
    void setUp() {
        this.profile = createProfile(1L, ProfileType.CLIENT.name());
        this.mappedProfile = new ProfileQueryPort(1L, ProfileType.CLIENT.name());
    }

    @Test
    void shouldFindProfileByIdSuccessfully() {
        Long profileId = 1L;

        when(this.repository.findById(profileId)).thenReturn(Optional.of(this.profile));
        when(this.mapper.toQuery(this.profile)).thenReturn(this.mappedProfile);

        var result = this.useCase.execute(profileId);

        assertNotNull(result);
        assertEquals(this.mappedProfile, result);
        verify(this.repository, times(1)).findById(profileId);
        verify(this.mapper, times(1)).toQuery(this.profile);
    }

    @Test
    void shouldThrowExceptionWhenProfileNotFound() {
        when(this.repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProfileNotFoundException.class, () -> {
            this.useCase.execute(1L);
        });

        verify(repository, times(1)).findById(1L);
        verify(this.mapper, never()).toQuery(any());
    }

    private Profile createProfile(Long id, String type) {
        return Profile.create(id, type);
    }
}