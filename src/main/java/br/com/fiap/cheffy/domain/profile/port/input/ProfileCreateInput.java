package br.com.fiap.cheffy.domain.profile.port.input;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputPort;

public interface ProfileCreateInput {

    Long create(ProfileInputPort profileInformation);
}
