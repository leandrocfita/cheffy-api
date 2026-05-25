package br.com.fiap.cheffy.infrastructure.adapters.in.records;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record InputOrderStatusRecord(
        @NotNull
        UUID orderId,

        @NotNull
        String status) {
}
