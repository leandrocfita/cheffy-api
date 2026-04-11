package br.com.fiap.cheffy.presentation.config.swagger.docs;

import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.presentation.config.doc_helper.*;
import br.com.fiap.cheffy.presentation.config.swagger.schema.ProfilePageResponseSchema;
import br.com.fiap.cheffy.presentation.dto.ProfileCreateReponseDto;
import br.com.fiap.cheffy.presentation.dto.ProfileInputDto;
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

@Tag(name = "3. Profiles", description = "Consulta e manutenção de perfis de usuário")
public interface ProfileControllerDocs {

    @Operation(summary = "Criar novo perfil", description = "Criar um novo perfil de usuário com base no tipo fornecido")
    @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso")
    @DefaultPublicApiErrors
    @DefaultConflictApiResponse
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProfileInputDto.class),
                    examples = @ExampleObject(
                            name = "Create Profile Request",
                            value = """
                                    {
                                      "name": "CHEF"
                                    }
                                    """
                    )
            )
    )
    ResponseEntity<ProfileCreateReponseDto> createProfile(@Valid ProfileInputDto profileInputDto);

    @Operation(summary = "Atualizar perfil por ID", description = "Atualiza um tipo de perfil existente identificado pelo seu ID")
    @ApiResponse(responseCode = "204", description = "Perfil atualizado com sucesso")
    @DefaultBadRequestApiResponse
    @DefaultNotFoundApiResponse
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    ResponseEntity<Void> updateProfileById(Long id, @Valid ProfileInputDto profileInputDto);

    @Operation(summary = "Atualizar perfil por nome", description = "Atualiza um tipo de perfil existente identificado pelo seu nome")
    @ApiResponse(responseCode = "204", description = "Perfil atualizado com sucesso")
    @DefaultBadRequestApiResponse
    @DefaultNotFoundApiResponse
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    ResponseEntity<Void> updateProfileByName(String name, @Valid ProfileInputDto profileInputDto);

    @Operation(summary = "Buscar perfil por ID", description = "Retorna os dados completos de um perfil específico")
    @ApiResponse(
            responseCode = "200",
            description = "Perfil encontrado com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProfileQueryPort.class)
            )
    )
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    ResponseEntity<ProfileQueryPort> findProfileById(Long id);

    @Operation(summary = "Listar todos os perfis")
    @ApiResponse(
            responseCode = "200",
            description = "Lista de perfis retornada com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProfilePageResponseSchema.class)
            )
    )
    @DefaultPublicApiErrors
    ResponseEntity<PageResult<ProfileQueryPort>> listAllProfiles(int page, int size, String sortBy, Sort.Direction direction);

    @Operation(summary = "Excluir perfil")
    @ApiResponse(responseCode = "204", description = "Perfil excluído com sucesso")
    @DefaultBadRequestApiResponse
    @DefaultConflictApiResponse
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    ResponseEntity<Void> deleteProfile(Long id);
}
