package br.com.fiap.cheffy.application.user.mapper;

import br.com.fiap.cheffy.application.address.mapper.AddressQueryMapper;
import br.com.fiap.cheffy.application.user.dto.AddressQueryPort;
import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.user.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserQueryMapper {

    private AddressQueryMapper addressQueryMapper;

    public UserQueryMapper(AddressQueryMapper addressQueryMapper) {
        this.addressQueryMapper = addressQueryMapper;
    }

    public UserQueryPort toQuery(User user) {
        return new UserQueryPort(
                user.getId().toString(),
                user.getName(),
                user.getEmail(),
                user.isActive(),
                toProfileTypes(user),
                toAddressQueries(user)
        );
    }

    private Set<ProfileType> toProfileTypes(User user) {
        return user.getProfiles().stream()
                .map(profile -> ProfileType.valueOf(profile.getType()))
                .collect(Collectors.toSet());
    }

    private Set<AddressQueryPort> toAddressQueries(User user) {
        return user.getAddresses().stream()
                .map(address -> addressQueryMapper.toQueryPort(address))
                .collect(Collectors.toSet());
    }
}
