package br.com.fiap.cheffy.presentation.config.swagger.docs;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultApiErrors;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultBadRequestApiResponse;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultConflictApiResponse;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultNotFoundApiResponse;
import br.com.fiap.cheffy.presentation.config.swagger.schema.FoodItemPageResponseSchema;
import br.com.fiap.cheffy.presentation.dto.FoodItemAvailabilityDTO;
import br.com.fiap.cheffy.presentation.dto.FoodItemDTO;
import br.com.fiap.cheffy.presentation.dto.FoodItemUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "5. Food Items", description = "Cadastro e manutenção dos itens de cardápio")
public interface FoodItemControllerDocs {

    @Operation(summary = "Listar itens do cardápio do restaurante", description = "Retorna lista paginada de itens do cardápio de um restaurante")
    @ApiResponse(
            responseCode = "200",
            description = "Lista retornada com sucesso",
            content = @Content(
                    mediaType = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FoodItemPageResponseSchema.class)
            )
    )
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    ResponseEntity<PageResult<FoodItemQueryPort>> listFoodItemsByRestaurant(UUID restaurantId, int page, int size, String sortBy, Sort.Direction direction, boolean includeInactive);

    @Operation(summary = "Create a new food item", description = "Creates a new food item associated with a specific restaurant")
    @ApiResponse(responseCode = "201", description = "Item criado no cardapio com sucesso")
    @DefaultBadRequestApiResponse
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    @DefaultConflictApiResponse
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FoodItemDTO.class),
                    examples = @ExampleObject(
                            name = "Create Food Item Request",
                            value = """
                                    {
                                      "name": "Prato Executivo",
                                      "description": "Arroz, feijao e carne",
                                      "price": 29.90,
                                      "photoKey": "prato-executivo.jpg",
                                      "deliveryAvailable": true,
                                      "available": true,
                                      "active": true
                                    }
                                    """
                    )
            )
    )
    ResponseEntity<FoodItemQueryPort> postFoodItem(@Valid FoodItemDTO foodItemDTO, @Valid UUID restaurantId);

    @Operation(summary = "Update an existing food item", description = "Updates an existing food item associated with a specific restaurant")
    @ApiResponse(responseCode = "204", description = "Item atualizado com sucesso")
    @DefaultBadRequestApiResponse
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    @DefaultConflictApiResponse
    ResponseEntity<Void> updateFoodItem(@Valid FoodItemUpdateDto foodItemUpdateDTO, @Valid UUID restaurantId, UUID userId, @Valid UUID foodItemId);

    @Operation(summary = "Buscar item do cardápio por ID", description = "Retorna os dados completos de um item específico do cardápio de um restaurante")
    @ApiResponse(
            responseCode = "200",
            description = "Item do cardapio encontrado com sucesso",
            content = @Content(
                    mediaType = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FoodItemQueryPort.class)
            )
    )
    @DefaultBadRequestApiResponse
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    ResponseEntity<FoodItemQueryPort> getFoodItemById(UUID restaurantId, UUID foodItemId);

    @Operation(summary = "Desativar food item de um restaurante")
    @ApiResponse(responseCode = "204", description = "Food item desativado com sucesso")
    @DefaultBadRequestApiResponse
    @DefaultNotFoundApiResponse
    @DefaultConflictApiResponse
    @ApiResponse(responseCode = "500", description = "Erro interno")
    ResponseEntity<Void> deactivateFoodItem(UUID restaurantId, UUID foodItemId);

    @Operation(summary = "Reativar food item de um restaurante")
    @ApiResponse(responseCode = "204", description = "Food item reativado com sucesso")
    @DefaultBadRequestApiResponse
    @DefaultNotFoundApiResponse
    @DefaultConflictApiResponse
    @ApiResponse(responseCode = "500", description = "Erro interno")
    ResponseEntity<Void> reactivateFoodItem(UUID restaurantId, UUID foodItemId);

    @Operation(summary = "Atualizar disponibilidade de um item do cardápio", description = "Atualiza available e/ou deliveryAvailable. Para habilitar deliveryAvailable, o item deve estar active e available. Para habilitar available, o item deve estar active.")
    @ApiResponse(responseCode = "204", description = "Disponibilidade atualizada com sucesso")
    @DefaultBadRequestApiResponse
    @DefaultNotFoundApiResponse
    @DefaultConflictApiResponse
    @ApiResponse(responseCode = "500", description = "Erro interno")
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FoodItemAvailabilityDTO.class),
                    examples = @ExampleObject(
                            name = "Update Availability Request",
                            value = """
                                    {
                                      "available": true,
                                      "deliveryAvailable": true
                                    }
                                    """
                    )
            )
    )
    ResponseEntity<Void> updateFoodItemAvailability(UUID restaurantId, UUID foodItemId, @Valid FoodItemAvailabilityDTO dto);
}
