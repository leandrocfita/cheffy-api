package br.com.fiap.cheffy.domain.order.entity;

import br.com.fiap.cheffy.domain.valueobject.Money;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Order {

    private final UUID id;
    private final UUID customerId;
    private final UUID restaurantId;
    private final List<OrderItem> items;
    private Money totalAmount;
    private OrderStatus status;

    private Order(UUID id, UUID customerId, UUID restaurantId, List<OrderItem> items, OrderStatus status
    ) {
        this.id = id;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.items = validateItems(items);
        this.status = status;
        this.totalAmount = calculateTotal(this.items);
    }

    public static Order create(UUID customerId, UUID restaurantId, List<OrderItem> items) {
        return new Order(null, customerId, restaurantId, items, OrderStatus.CREATED);
    }

    public static Order reconstitute(UUID id, UUID customerId, UUID restaurantId, List<OrderItem> items, OrderStatus status) {
        return new Order(id, customerId, restaurantId, items, status);
    }

    private List<OrderItem> validateItems(List<OrderItem> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter pelo menos um item");
        }

        if (items.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Os itens do pedido não podem conter elementos nulos");
        }

        return List.copyOf(items);
    }

    private Money calculateTotal(List<OrderItem> items) {
        Money total = new Money(BigDecimal.ZERO);

        for (OrderItem item : items) {
            total = total.add(item.subtotal());
        }

        return total;
    }

    public void markPaymentPending() {
        this.status = OrderStatus.PAYMENT_PENDING;
    }

    public void markAsPaid() {
        this.status = OrderStatus.PAID;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
