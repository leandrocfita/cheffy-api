package br.com.fiap.cheffy.presentation.config.swagger.schema;

import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "UserPageResponse")
public record UserPageResponseSchema(
        List<UserQueryPort> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last,
        boolean empty
) {
}
