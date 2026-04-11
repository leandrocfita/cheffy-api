package br.com.fiap.cheffy.domain.user.port.input;

import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;

import java.util.UUID;

public interface UpdateAddressInput {

    void execute(UUID userId, Long addressId, AddressCommandPort addressCmPort);
}
