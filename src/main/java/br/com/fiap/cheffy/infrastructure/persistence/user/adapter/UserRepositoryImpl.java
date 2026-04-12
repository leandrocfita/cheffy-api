package br.com.fiap.cheffy.infrastructure.persistence.user.adapter;

import br.com.fiap.cheffy.infrastructure.persistence.pagination.PageMapper;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
import br.com.fiap.cheffy.infrastructure.persistence.user.entity.UserJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.user.mapper.UserPersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.user.repository.UserJpaRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper mapper;

    @Override
    public User save(User user) {
        var jpaEntity = mapper.toJpa(user);

        UserJpaEntity saved = userJpaRepository.save(jpaEntity);

        return mapper.toDomain(saved);
    }


    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public PageResult<User> findAll(PageRequest pageRequest) {
        Pageable springPageable = PageMapper.toSpringPageable(pageRequest);
        Page<UserJpaEntity> springPage = userJpaRepository.findAll(springPageable);
        Page<User> domainPage = springPage.map(mapper::toDomain);

        return PageMapper.toDomainPageResult(domainPage);
    }

    @Override
    public PageResult<User> findByName(String name, PageRequest pageRequest) {
        Pageable springPageable = PageMapper.toSpringPageable(pageRequest);
        Page<UserJpaEntity> springPage = userJpaRepository.findByNameContainingIgnoreCase(name, springPageable);
        Page<User> domainPage = springPage.map(mapper::toDomain);

        return PageMapper.toDomainPageResult(domainPage);
    }

}
