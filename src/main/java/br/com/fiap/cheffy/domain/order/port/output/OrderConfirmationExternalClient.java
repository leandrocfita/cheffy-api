package br.com.fiap.cheffy.domain.order.port.output;

import br.com.fiap.cheffy.application.order.dto.OrderConfirmationCommandPort;

public interface OrderConfirmationExternalClient {

    void confirm(OrderConfirmationCommandPort command);
}
