package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.domain.order.port.input.CreateOrderInput;
import br.com.fiap.cheffy.presentation.dto.OrderCreateDTO;
import br.com.fiap.cheffy.presentation.mapper.OrderWebMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private final CreateOrderInput createOrderInput;
    private final OrderWebMapper orderWebMapper;

    public OrderController(CreateOrderInput createOrderInput, OrderWebMapper orderWebMapper) {
        this.createOrderInput = createOrderInput;
        this.orderWebMapper = orderWebMapper;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody @Valid OrderCreateDTO orderCreateDTO, @RequestAttribute("userId") UUID userId) {
        log.info("OrderController.createOrder - START - Create order for user [{}]", userId);

        String createdId = createOrderInput.execute(orderWebMapper.toCommand(orderCreateDTO), userId);

        log.info("OrderController.createOrder - END - Order created with id [{}]", createdId);

        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }
}
