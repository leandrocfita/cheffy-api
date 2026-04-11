package br.com.fiap.cheffy.presentation.mapper;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputPort;
import br.com.fiap.cheffy.presentation.dto.ProfileInputDto;

public class ProfileWebMapper {

    public static ProfileInputPort toProfileInputCommandPort(ProfileInputDto profileInputDto) {

        return new ProfileInputPort(profileInputDto.profileNameType());
    }
}
