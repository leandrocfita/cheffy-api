package br.com.fiap.cheffy.application.profile.mapper;

import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import br.com.fiap.cheffy.domain.profile.entity.Profile;

public class ProfileQueryMapper {

    public ProfileQueryPort toQuery(Profile profile) {
        return new ProfileQueryPort(
                profile.getId(),
                profile.getType()
        );
    }
}
