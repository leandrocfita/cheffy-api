package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.order.dto.CreateOrderResultPort;
import br.com.fiap.cheffy.application.order.dto.OrderQueryPort;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.order.port.input.CreateOrderInput;
import br.com.fiap.cheffy.domain.order.port.input.FindOrderByIdInput;
import br.com.fiap.cheffy.domain.order.port.input.ListOrdersByCustomerInput;
import br.com.fiap.cheffy.presentation.config.swagger.docs.OrderControllerDocs;
import br.com.fiap.cheffy.presentation.dto.OrderCreateDTO;
import br.com.fiap.cheffy.presentation.mapper.OrderWebMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController implements OrderControllerDocs {

    private final CreateOrderInput createOrderInput;
    private final FindOrderByIdInput findOrderByIdInput;
    private final ListOrdersByCustomerInput listOrdersByCustomerInput;
    private final OrderWebMapper orderWebMapper;

    public OrderController(
            CreateOrderInput createOrderInput,
            FindOrderByIdInput findOrderByIdInput,
            ListOrdersByCustomerInput listOrdersByCustomerInput,
            OrderWebMapper orderWebMapper
    ) {
        this.createOrderInput = createOrderInput;
        this.findOrderByIdInput = findOrderByIdInput;
        this.listOrdersByCustomerInput = listOrdersByCustomerInput;
        this.orderWebMapper = orderWebMapper;
    }

    @PostMapping
    @Override
    public ResponseEntity<CreateOrderResultPort> createOrder(@RequestBody @Valid OrderCreateDTO orderCreateDTO, @RequestAttribute("userId") UUID userId) {
        log.info("OrderController.createOrder - START - Create order for user [{}]", userId);

        CreateOrderResultPort createdOrder = createOrderInput.execute(orderWebMapper.toCommand(orderCreateDTO), userId);

        log.info("OrderController.createOrder - END - Order created with id [{}]", createdOrder.orderId());

        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    @Override
    public ResponseEntity<OrderQueryPort> findById(
            @PathVariable UUID orderId,
            @RequestAttribute("userId") UUID userId
    ) {
        return ResponseEntity.ok(findOrderByIdInput.execute(orderId, userId));
    }

    @GetMapping
    @Override
    public ResponseEntity<PageResult<OrderQueryPort>> listByCustomer(
            @RequestAttribute("userId") UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        PageRequest.SortDirection sortDirection = direction == Sort.Direction.DESC
                ? PageRequest.SortDirection.DESC
                : PageRequest.SortDirection.ASC;
        return ResponseEntity.ok(listOrdersByCustomerInput.execute(userId, PageRequest.of(page, size, sortBy, sortDirection)));
    }
}
