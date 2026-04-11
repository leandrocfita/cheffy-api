package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputPort;
import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.exception.ProfileIsOwnerOrClientException;
import br.com.fiap.cheffy.domain.profile.exception.ProfileNotFoundException;
import br.com.fiap.cheffy.domain.profile.port.input.*;
import br.com.fiap.cheffy.presentation.dto.ProfileCreateReponseDto;
import br.com.fiap.cheffy.presentation.dto.ProfileInputDto;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock
    private ProfileCreateInput profileCreateInput;

    @Mock
    private ProfileUpdateInput profileUpdateInput;

    @Mock
    private FindProfileByInput findProfileByInput;

    @Mock
    private ListAllProfilesInput listAllProfilesInput;

    @Mock
    private ProfileDeleteInput profileDeleteInput;

    @InjectMocks
    private ProfileController profileController;

    @Test
    @DisplayName("Should return 201 Created when profile is created successfully")
    void createProfileReturnsCreated() {
        ProfileInputDto inputDto = new ProfileInputDto("Chef");
        Long createdId = 1L;
        when(profileCreateInput.create(any(ProfileInputPort.class))).thenReturn(createdId);

        ResponseEntity<ProfileCreateReponseDto> response = profileController.createProfile(inputDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isInstanceOf(ProfileCreateReponseDto.class);
        ProfileCreateReponseDto responseBody = (ProfileCreateReponseDto) response.getBody();
        Assertions.assertNotNull(responseBody);
        assertThat(responseBody.id()).isEqualTo(createdId);
        assertThat(responseBody.nameType()).isEqualTo("Chef");
    }

    @Test
    @DisplayName("Should return 204 No Content when profile is updated by ID successfully")
    void updateProfileByIdReturnsNoContent() {
        Long id = 1L;
        ProfileInputDto inputDto = new ProfileInputDto("Chef");

        ResponseEntity<Void> response = profileController.updateProfileById(id, inputDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(profileUpdateInput).updateById(eq(id), any(ProfileInputPort.class));
    }

    @Test
    @DisplayName("Should return 204 No Content when profile is updated by Name successfully")
    void updateProfileByNameReturnsNoContent() {
        String name = "Client";
        ProfileInputDto inputDto = new ProfileInputDto("Chef");

        ResponseEntity<Void> response = profileController.updateProfileByName(name, inputDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(profileUpdateInput).updateByName(eq(name), any(ProfileInputPort.class));
        var body = response.getBody();
        assertThat(body).isNull();
    }

    @Test
    void findProfileByIdReturnsOk() {
        Long id = 1L;
        ProfileQueryPort queryPort = new ProfileQueryPort(1L, ProfileType.CLIENT.name());
        when(findProfileByInput.execute(eq(id))).thenReturn(queryPort);

        var response = profileController.findProfileById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(queryPort);
        verify(findProfileByInput).execute(eq(id));
    }

    @Test
    void listAllProfilesReturnsOk() {
        ProfileQueryPort queryPort = new ProfileQueryPort(1L, ProfileType.CLIENT.name());
        var profilePage = PageResult.of(List.of(queryPort), 0, 10, 1);
        when(listAllProfilesInput.execute(any())).thenReturn(profilePage);

        var response = profileController.listAllProfiles(0, 10, "type", Sort.Direction.ASC);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(listAllProfilesInput, times(1)).execute(any());
    }

    @Test
    @DisplayName("Should return 204 No Content when deleting an existing profile id")
    void deleteProfileExistingIdReturnsNoContent() {
        Long id = 1L;

        var response = profileController.deleteProfile(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(profileDeleteInput).execute(id);
    }

    @Test
    @DisplayName("Should throw ProfileNotFoundException when deleting a non-existing profile id")
    void deleteProfileNonExistingIdThrowsProfileNotFoundException() {
        Long id = 999L;
        doThrow(new ProfileNotFoundException(ExceptionsKeys.PROFILE_NOT_FOUND_EXCEPTION, id.toString()))
                .when(profileDeleteInput).execute(id);

        Assertions.assertThrows(ProfileNotFoundException.class, () -> profileController.deleteProfile(id));

        verify(profileDeleteInput).execute(id);
    }

    @Test
    @DisplayName("Should throw ProfileIsOwnerOrClientException when trying to delete CLIENT or OWNER profile")
    void deleteProfileClientThrowsProfileIsOwnerOrClientException() {
        Long clientProfileId = 2L;
        doThrow(new ProfileIsOwnerOrClientException(ExceptionsKeys.PROFILE_IS_OWNER_OR_CLIENT))
                .when(profileDeleteInput).execute(clientProfileId);

        Assertions.assertThrows(ProfileIsOwnerOrClientException.class,
                () -> profileController.deleteProfile(clientProfileId));

        verify(profileDeleteInput).execute(clientProfileId);
    }
}
