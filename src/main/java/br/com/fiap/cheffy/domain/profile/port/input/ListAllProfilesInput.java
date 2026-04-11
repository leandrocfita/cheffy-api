package br.com.fiap.cheffy.domain.profile.port.input;

import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;

public interface ListAllProfilesInput {

    PageResult<ProfileQueryPort> execute(PageRequest pageRequest);
}
