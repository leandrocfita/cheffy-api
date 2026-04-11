package br.com.fiap.cheffy.application.profile.usecase;

import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import br.com.fiap.cheffy.application.profile.mapper.ProfileQueryMapper;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListAllProfilesUseCaseTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileQueryMapper mapper;

    private ListAllProfilesUseCase useCase;

    private int page;
    private int size;

    private Profile firstProfile;
    private Profile secondProfile;
    private List<Profile> profiles;

    private ProfileQueryPort mappedFirstProfile;
    private ProfileQueryPort mappedSecondProfile;


    @BeforeEach
    void setUp() {
        useCase = new ListAllProfilesUseCase(profileRepository, mapper);

        this.page = 0;
        this.size = 10;

        this.firstProfile = createProfile(1L, ProfileType.CLIENT.name());
        this.secondProfile = createProfile(2L, ProfileType.OWNER.name());
        this.profiles = Arrays.asList(this.firstProfile, this.secondProfile);

        this.mappedFirstProfile = new ProfileQueryPort(1L, ProfileType.CLIENT.name());
        this.mappedSecondProfile = new ProfileQueryPort(2L, ProfileType.OWNER.name());

    }

    @Test
    void shouldListProfilesSuccessfully() {
        PageRequest pageRequest = PageRequest.of(this.page, this.size, "type", PageRequest.SortDirection.ASC);

        var profilePage = PageResult.of(this.profiles, this.page, this.size, 2);

        when(profileRepository.findAll(pageRequest)).thenReturn(profilePage);
        when(mapper.toQuery(this.firstProfile)).thenReturn(this.mappedFirstProfile);
        when(mapper.toQuery(this.secondProfile)).thenReturn(this.mappedSecondProfile);

        var profilesPageResult = useCase.execute(pageRequest);

        assertNotNull(profilesPageResult);
        assertEquals(2, profilesPageResult.totalElements());
        assertEquals(2, profilesPageResult.numberOfElements());
        verify(profileRepository, times(1)).findAll(pageRequest);
    }

    @Test
    void shouldReturnEmptyListWhenNoUsers() {
        PageRequest pageRequest = PageRequest.of(this.page, this.size, "type", PageRequest.SortDirection.ASC);
        PageResult<Profile> emptyPage = PageResult.of(Collections.emptyList(), this.page, this.size, 0);

        when(profileRepository.findAll(pageRequest)).thenReturn(emptyPage);

        var profilesPageResult = useCase.execute(pageRequest);

        assertNotNull(profilesPageResult);
        assertEquals(0, profilesPageResult.totalElements());
        assertEquals(0, profilesPageResult.numberOfElements());
        assertTrue(profilesPageResult.empty());
        assertTrue(profilesPageResult.first());
        assertTrue(profilesPageResult.last());

        verify(profileRepository, times(1)).findAll(pageRequest);
        verify(mapper, never()).toQuery(any());
    }

    @Test
    void shouldHandlePagination(){
        PageRequest pageRequest = PageRequest.of(this.page, this.size, "type", PageRequest.SortDirection.ASC);

        var profilePage = PageResult.of(this.profiles, 1, 2, 5);

        ProfileQueryPort queryPort3 = mock(ProfileQueryPort.class);
        ProfileQueryPort queryPort4 = mock(ProfileQueryPort.class);

        when(profileRepository.findAll(pageRequest)).thenReturn(profilePage);
        when(mapper.toQuery(this.firstProfile)).thenReturn(queryPort3);
        when(mapper.toQuery(this.secondProfile)).thenReturn(queryPort4);

        var profilesPageResult = useCase.execute(pageRequest);

        assertNotNull(profilesPageResult);
        assertEquals(5, profilesPageResult.totalElements());
        assertEquals(2, profilesPageResult.numberOfElements());
        assertEquals(3, profilesPageResult.totalPages());
        assertEquals(1, profilesPageResult.page());
        assertFalse(profilesPageResult.first());
        assertFalse(profilesPageResult.last());
        assertFalse(profilesPageResult.empty());

        verify(profileRepository, times(1)).findAll(pageRequest);
    }

    private Profile createProfile(Long id, String type) {
        return Profile.create(id, type);
    }
}