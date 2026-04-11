package br.com.fiap.cheffy.domain.profile.port.input;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputPort;

public interface ProfileUpdateInput {

    void updateById (Long id, ProfileInputPort profileInputPort);
    void updateByName(String nameType, ProfileInputPort profileInputPort);
}
