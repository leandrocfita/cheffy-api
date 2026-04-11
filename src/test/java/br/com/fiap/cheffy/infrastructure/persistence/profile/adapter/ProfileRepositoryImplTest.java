package br.com.fiap.cheffy.infrastructure.persistence.profile.adapter;

import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.infrastructure.persistence.profile.entity.ProfileJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.profile.mapper.ProfilePersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.profile.repository.ProfileJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ProfileRepositoryImplTest {

    @Mock
    private ProfileJpaRepository jpaRepository;

    @Mock
    private ProfilePersistenceMapper mapper;

    @InjectMocks
    private ProfileRepositoryImpl profileRepository;

    @Test
    void findById() {
        Profile profile = Profile.create(1L, ProfileType.CLIENT.name());
        ProfileJpaEntity jpaEntity = new ProfileJpaEntity();
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(jpaEntity));
        when(mapper.toDomain(jpaEntity)).thenReturn(profile);

        Optional<Profile> result = profileRepository.findById(1L);

        assertThat(result).isPresent();
    }

    @Test
    void findByType() {
        Profile profile = Profile.create(1L, ProfileType.CLIENT.name());
        ProfileJpaEntity jpaEntity = new ProfileJpaEntity();
        when(jpaRepository.findByType("CLIENT")).thenReturn(Optional.of(jpaEntity));
        when(mapper.toDomain(jpaEntity)).thenReturn(profile);

        Optional<Profile> result = profileRepository.findByType("CLIENT");

        assertThat(result).isPresent();
    }

    @Test
    void saveProfileDelegatesToJpaRepository() {
        Profile profile = Profile.create(1L, ProfileType.CLIENT.name());
        ProfileJpaEntity jpaEntity = new ProfileJpaEntity();
        jpaEntity.setId(1L);
        when(mapper.toJpaReference(profile)).thenReturn(jpaEntity);
        when(jpaRepository.save(jpaEntity)).thenReturn(jpaEntity);

        Long result = profileRepository.save(profile);

        assertThat(result).isEqualTo(1L);
    }

    @Test
    void findAll() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        ProfileJpaEntity entity = new ProfileJpaEntity();
        Profile profile = Profile.create(1L, ProfileType.CLIENT.name());
        Page<ProfileJpaEntity> springPage = new PageImpl<>(List.of(entity));
        when(jpaRepository.findAll(any(Pageable.class))).thenReturn(springPage);
        when(mapper.toDomain(entity)).thenReturn(profile);

        var result = profileRepository.findAll(pageRequest);

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().getFirst()).isEqualTo(profile);
    }

    @Test
    void deleteShouldMapAndDelegateToJpaRepository() {
        Profile profile = Profile.create(1L, ProfileType.CLIENT.name());
        ProfileJpaEntity jpaEntity = new ProfileJpaEntity();

        when(mapper.toJpaReference(profile)).thenReturn(jpaEntity);

        profileRepository.delete(profile);

        verify(mapper).toJpaReference(profile);
        verify(jpaRepository).delete(jpaEntity);
    }

    @Test
    void deleteShouldPropagateExceptionWhenJpaDeleteFails() {
        Profile profile = Profile.create(1L, ProfileType.CLIENT.name());
        ProfileJpaEntity jpaEntity = new ProfileJpaEntity();

        when(mapper.toJpaReference(profile)).thenReturn(jpaEntity);
        doThrow(new RuntimeException("delete failed"))
                .when(jpaRepository).delete(jpaEntity);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> profileRepository.delete(profile)
        );

        assertThat(exception).hasMessage("delete failed");
        verify(mapper).toJpaReference(profile);
        verify(jpaRepository).delete(jpaEntity);
    }
}
