package br.com.fiap.cheffy.domain.order.entity;

import br.com.fiap.cheffy.shared.exception.InvalidDataException;
import br.com.fiap.cheffy.shared.exception.OperationNotAllowedException;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.ORDER_STATUS_CANNOT_BE_NULL;
import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.ORDER_STATUS_NOT_ALLOWED;

public enum OrderStatus {
    CREATED("criado"),
    PAID("pago"),
    SENT_TO_PAYMENT_GATEWAY("enviado"),
    PENDING("pendente"),
    CANCELED("cancelado");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }

    public static OrderStatus fromStatus(String statusReceived) {
        if (statusReceived == null) {
            throw new InvalidDataException(ORDER_STATUS_CANNOT_BE_NULL);
        }

        try {
            for (OrderStatus s : values()) {
                if (s.getStatus().equalsIgnoreCase(statusReceived)) {
                    return s;
                }
            }
            throw new OperationNotAllowedException(ORDER_STATUS_NOT_ALLOWED);
        } catch (OperationNotAllowedException ignored) {
            throw new OperationNotAllowedException(ORDER_STATUS_NOT_ALLOWED);
        }
    }
}
