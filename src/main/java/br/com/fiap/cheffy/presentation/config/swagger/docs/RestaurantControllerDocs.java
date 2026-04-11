package br.com.fiap.cheffy.presentation.config.swagger.docs;

import br.com.fiap.cheffy.application.restaurant.dto.RestaurantQueryPort;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultApiErrors;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultBadRequestApiResponse;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultConflictApiResponse;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultNotFoundApiResponse;
import br.com.fiap.cheffy.presentation.dto.RestaurantCreateDTO;
import br.com.fiap.cheffy.presentation.dto.RestaurantUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "4. Restaurants", description = "Cadastro, consulta e manutenção de restaurantes")
public interface RestaurantControllerDocs {

    @Operation(summary = "Cadastrar um novo restaurante para um usuário", description = "Cria um novo restaurante, tendo o usuário enviado como proprietário. O usuário recebe o perfil de OWNER")
    @ApiResponse(
            responseCode = "201",
            description = "Restaurante criado com sucesso - Retorna UUID do novo restaurante",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(type = "string", format = "uuid")
            )
    )
    @DefaultBadRequestApiResponse
    @DefaultApiErrors
    @DefaultConflictApiResponse
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RestaurantCreateDTO.class),
                    examples = @ExampleObject(
                            name = "Create Restaurant Request",
                            value = """
                                    {
                                      "name": "Restaurante Central",
                                      "culinary": "Brasileira",
                                      "cnpj": "27865757000102",
                                      "openingTime": "09:00",
                                      "closingTime": "22:00",
                                      "zoneId": "America/Sao_Paulo",
                                      "open24hours": false,
                                      "address": {
                                        "streetName": "Avenida Paulista",
                                        "number": 1000,
                                        "city": "Sao Paulo",
                                        "postalCode": "01310000",
                                        "neighborhood": "Bela Vista",
                                        "stateProvince": "SP",
                                        "addressLine": "Loja 1"
                                      }
                                    }
                                    """
                    )
            )
    )
    ResponseEntity<String> registerRestaurant(@Valid RestaurantCreateDTO restaurantCreateDTO, @Valid UUID userId);

    @Operation(summary = "Desativar restaurante de um usuário")
    @ApiResponse(responseCode = "204", description = "Restaurante desativado com sucesso")
    @DefaultBadRequestApiResponse
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    ResponseEntity<Void> deactivateRestaurant(@Valid UUID id, @Valid UUID userId);

    @Operation(summary = "Reativar restaurante de um usuário")
    @ApiResponse(responseCode = "204", description = "Restaurante reativado com sucesso")
    @DefaultBadRequestApiResponse
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    ResponseEntity<Void> reactivateRestaurant(@Valid UUID id, @Valid UUID userId);

    @Operation(summary = "Atualizar dados de um restaurante", description = "Atualiza os dados de um restaurante existente. Somente o proprietário pode atualizar. Restaurantes desativados não podem ser atualizados.")
    @ApiResponse(responseCode = "204", description = "Restaurante atualizado com sucesso")
    @DefaultBadRequestApiResponse
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    @DefaultConflictApiResponse
    ResponseEntity<Void> updateRestaurant(UUID id, UUID userId, @Valid RestaurantUpdateDTO restaurantUpdateDTO);

    @Operation(summary = "Buscar restaurante por ID", description = "Retorna os dados completos de um restaurante com seu cardápio")
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    @ApiResponse(
            responseCode = "200",
            description = "Restaurante encontrado com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RestaurantQueryPort.class)
            )
    )
    ResponseEntity<RestaurantQueryPort> findRestaurantById(UUID id);
}
