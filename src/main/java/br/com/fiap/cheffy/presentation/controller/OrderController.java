package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.order.dto.CreateOrderResultPort;
import br.com.fiap.cheffy.application.order.dto.OrderQueryPort;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.order.port.input.ConfirmOrderInput;
import br.com.fiap.cheffy.domain.order.port.input.CreateOrderInput;
import br.com.fiap.cheffy.domain.order.port.input.FindOrderByIdInput;
import br.com.fiap.cheffy.domain.order.port.input.ListOrdersByCustomerInput;
import br.com.fiap.cheffy.infrastructure.security.model.CurrentUser;
import br.com.fiap.cheffy.infrastructure.security.resolver.CurrentUserMapper;
import br.com.fiap.cheffy.presentation.config.swagger.docs.OrderControllerDocs;
import br.com.fiap.cheffy.presentation.dto.OrderCreateDTO;
import br.com.fiap.cheffy.presentation.mapper.OrderWebMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final ConfirmOrderInput confirmOrderInput;
    private final FindOrderByIdInput findOrderByIdInput;
    private final ListOrdersByCustomerInput listOrdersByCustomerInput;
    private final OrderWebMapper orderWebMapper;
    private final CurrentUserMapper currentUserMapper;

    public OrderController(
            CreateOrderInput createOrderInput,
            ConfirmOrderInput confirmOrderInput,
            FindOrderByIdInput findOrderByIdInput,
            ListOrdersByCustomerInput listOrdersByCustomerInput,
            OrderWebMapper orderWebMapper,
            CurrentUserMapper currentUserMapper
    ) {
        this.createOrderInput = createOrderInput;
        this.confirmOrderInput = confirmOrderInput;
        this.findOrderByIdInput = findOrderByIdInput;
        this.listOrdersByCustomerInput = listOrdersByCustomerInput;
        this.orderWebMapper = orderWebMapper;
        this.currentUserMapper = currentUserMapper;
    }

    @PostMapping
    @Override
    public ResponseEntity<CreateOrderResultPort> createOrder(
            @RequestBody @Valid OrderCreateDTO orderCreateDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        CurrentUser currentUser = currentUserMapper.from(jwt);
        log.info("HTTP request received to create order for user [userId={}]", currentUser.id());
        CreateOrderResultPort createdOrder = createOrderInput.execute(orderWebMapper.toCommand(orderCreateDTO), currentUser.id());
        log.info("Order created successfully for user [userId={}]", currentUser.id());
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @PatchMapping("/{orderId}/confirm")
    @Override
    public ResponseEntity<OrderQueryPort> confirmOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        CurrentUser currentUser = currentUserMapper.from(jwt);
        log.info("HTTP request received to confirm order [orderId={}] for user [userId={}]", orderId, currentUser.id());
        OrderQueryPort confirmedOrder = confirmOrderInput.execute(
                orderId,
                currentUser.id()
        );
        log.info("Order [{}] confirmed successfully for user [userId={}]", confirmedOrder.id(), currentUser.id());
        return ResponseEntity.ok(confirmedOrder);
    }

    @GetMapping("/{orderId}")
    @Override
    public ResponseEntity<OrderQueryPort> findById(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        CurrentUser currentUser = currentUserMapper.from(jwt);
        log.info("HTTP request received to find order by id [orderId={}] for user [userId={}]", orderId, currentUser.id());
        var order = findOrderByIdInput.execute(orderId, currentUser.id());
        log.info("Order found successfully [orderId={}] for user [userId={}]", orderId, currentUser.id());
        return ResponseEntity.ok(order);
    }

    @GetMapping
    @Override
    public ResponseEntity<PageResult<OrderQueryPort>> listByCustomer(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        CurrentUser currentUser = currentUserMapper.from(jwt);
        log.info("HTTP request received to search all orders [userId={}, page={}, size={}, sortBy={}, direction={}]", currentUser.id(), page, size, sortBy, direction);
        PageRequest.SortDirection sortDirection = direction == Sort.Direction.DESC
                ? PageRequest.SortDirection.DESC
                : PageRequest.SortDirection.ASC;
        var order = listOrdersByCustomerInput.execute(currentUser.id(), PageRequest.of(page, size, sortBy, sortDirection));
        log.info("Orders found successfully for user [userId={}], {} orders were found on page {}", currentUser.id(), order.numberOfElements(), page);
        return ResponseEntity.ok(order);
    }
}
