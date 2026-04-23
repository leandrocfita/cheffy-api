package br.com.fiap.cheffy.presentation.config.swagger.docs;

import br.com.fiap.cheffy.application.order.dto.CreateOrderResultPort;
import br.com.fiap.cheffy.application.order.dto.OrderQueryPort;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultApiErrors;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultBadRequestApiResponse;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultNotFoundApiResponse;
import br.com.fiap.cheffy.presentation.dto.OrderCreateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "6. Orders", description = "Criação e consulta de pedidos")
public interface OrderControllerDocs {

    @Operation(summary = "Criar pedido", description = "Cria um pedido para o cliente autenticado, calculando o total a partir dos itens do cardápio")
    @ApiResponse(
            responseCode = "201",
            description = "Pedido criado com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CreateOrderResultPort.class)
            )
    )
    @DefaultBadRequestApiResponse
    @DefaultNotFoundApiResponse
    @DefaultApiErrors
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = OrderCreateDTO.class),
                    examples = @ExampleObject(
                            name = "Create Order Request",
                            value = """
                                    {
                                      "restaurantId": "00000000-0000-0000-0000-000000000001",
                                      "items": [
                                        {
                                          "foodItemId": "00000000-0000-0000-0000-000000000002",
                                          "quantity": 2
                                        }
                                      ]
                                    }
                                    """
                    )
            )
    )
    ResponseEntity<CreateOrderResultPort> createOrder(@Valid OrderCreateDTO orderCreateDTO, UUID userId);

    @Operation(summary = "Consultar pedido por ID", description = "Retorna um pedido associado ao cliente autenticado")
    @ApiResponse(
            responseCode = "200",
            description = "Pedido encontrado com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = OrderQueryPort.class)
            )
    )
    @DefaultNotFoundApiResponse
    @DefaultApiErrors
    ResponseEntity<OrderQueryPort> findById(UUID orderId, UUID userId);

    @Operation(summary = "Listar pedidos do cliente autenticado", description = "Retorna lista paginada dos pedidos associados ao cliente autenticado")
    @ApiResponse(
            responseCode = "200",
            description = "Pedidos retornados com sucesso",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @DefaultApiErrors
    ResponseEntity<PageResult<OrderQueryPort>> listByCustomer(UUID userId, int page, int size, String sortBy, Sort.Direction direction);
}
