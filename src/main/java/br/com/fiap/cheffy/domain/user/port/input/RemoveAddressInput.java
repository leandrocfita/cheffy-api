package br.com.fiap.cheffy.domain.user.port.input;

import java.util.UUID;

public interface RemoveAddressInput {

    void execute(UUID userId, Long addressId);
}
