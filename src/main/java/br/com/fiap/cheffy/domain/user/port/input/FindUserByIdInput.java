package br.com.fiap.cheffy.domain.user.port.input;

import br.com.fiap.cheffy.application.user.dto.UserQueryPort;

import java.util.UUID;

public interface FindUserByIdInput {
    
    UserQueryPort execute(UUID id);
}