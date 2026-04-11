package br.com.fiap.cheffy.domain.profile.port.input;

import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;

public interface FindProfileByInput {

    ProfileQueryPort execute(Long id);
}
