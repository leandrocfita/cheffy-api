package br.com.fiap.cheffy.presentation.config.swagger.schema;

import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "ProfilePageResponse")
public record ProfilePageResponseSchema(
        List<ProfileQueryPort> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last,
        boolean empty
) {
}
