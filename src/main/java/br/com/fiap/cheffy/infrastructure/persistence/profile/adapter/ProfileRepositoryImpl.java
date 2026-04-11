package br.com.fiap.cheffy.infrastructure.persistence.profile.adapter;

import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.infrastructure.persistence.pagination.PageMapper;
import br.com.fiap.cheffy.infrastructure.persistence.profile.entity.ProfileJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.profile.mapper.ProfilePersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.profile.repository.ProfileJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepository {

    private final ProfileJpaRepository profileJpaRepository;
    private final ProfilePersistenceMapper mapper;

    @Override
    public Optional<Profile> findById(Long id) {
        return profileJpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Profile> findByType(String type) {
        return profileJpaRepository.findByType(type)
                .map(mapper::toDomain);
    }

    @Override
    public Long save(Profile profileDomain) {
        ProfileJpaEntity profileJpa = mapper.toJpaReference(profileDomain);
        return profileJpaRepository.save(profileJpa).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<Profile> findAll(PageRequest pageRequest) {
        Pageable springPageable = PageMapper.toSpringPageable(pageRequest);
        Page<ProfileJpaEntity> springPage = profileJpaRepository.findAll(springPageable);
        Page<Profile> domainPage = springPage.map(mapper::toDomain);

        return PageMapper.toDomainPageResult(domainPage);
    }

    @Override
    public void delete(Profile profileDomain) {
        ProfileJpaEntity profileJpa = mapper.toJpaReference(profileDomain);
        profileJpaRepository.delete(profileJpa);
    }
}
