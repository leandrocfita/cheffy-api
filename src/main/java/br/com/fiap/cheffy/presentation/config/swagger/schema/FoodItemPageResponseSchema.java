package br.com.fiap.cheffy.presentation.config.swagger.schema;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "FoodItemPageResponse")
public record FoodItemPageResponseSchema(
        List<FoodItemQueryPort> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last,
        boolean empty
) {
}
