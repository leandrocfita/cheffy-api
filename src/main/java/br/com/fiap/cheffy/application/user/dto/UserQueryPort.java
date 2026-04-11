package br.com.fiap.cheffy.application.user.dto;

import br.com.fiap.cheffy.domain.profile.ProfileType;

import java.util.Set;

public record UserQueryPort(
        String id,
        String name,
        String email,
        boolean active,
        Set<ProfileType> profileType,
        Set<AddressQueryPort> addresses
){}
