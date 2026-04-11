package br.com.fiap.cheffy.infrastructure.persistence.user.mapper;

import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.infrastructure.persistence.address.mapper.AddressPersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.profile.mapper.ProfilePersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.user.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserPersistenceMapper {

    private final ProfilePersistenceMapper profileMapper;
    private final AddressPersistenceMapper addressMapper;

    public UserPersistenceMapper(
            ProfilePersistenceMapper profileMapper,
            AddressPersistenceMapper addressMapper) {
        this.profileMapper = profileMapper;
        this.addressMapper = addressMapper;
    }

    public UserJpaEntity toJpa(User user) {
        UserJpaEntity jpa = new UserJpaEntity();

        jpa.setId(user.getId());
        jpa.setName(user.getName());
        jpa.setEmail(user.getEmail());
        jpa.setLogin(user.getLogin());
        jpa.setPassword(user.getPassword());
        jpa.setActive(user.isActive());

        jpa.setProfiles(
                user.getProfiles().stream()
                        .map(profileMapper::toJpaReference)
                        .collect(Collectors.toSet())
        );

        jpa.setAddresses(
                user.getAddresses().stream()
                        .map(addressMapper::toJpa)
                        .collect(Collectors.toSet())
        );

        return jpa;
    }

    public User toDomain(UserJpaEntity jpa) {
        User user = new User(
                jpa.getId(),
                jpa.getName(),
                jpa.getEmail(),
                jpa.getLogin(),
                jpa.getPassword(),
                jpa.isActive()
        );

        jpa.getProfiles().forEach(p ->
                user.addProfile(profileMapper.toDomain(p))
        );

        jpa.getAddresses().forEach(a ->
                user.addAddress(addressMapper.toDomain(a))
        );

        return user;
    }
}
