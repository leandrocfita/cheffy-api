package br.com.fiap.cheffy.domain.order.entity;

import br.com.fiap.cheffy.domain.valueobject.Money;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class OrderItem {

    private final UUID foodItemId;
    private final String name;
    private final int quantity;
    private final Money price;

    private OrderItem(UUID foodItemId, String name, Integer quantity, BigDecimal price) {
        this.foodItemId = foodItemId;
        this.name = name;
        this.quantity = validateQuantity(quantity);
        this.price = new Money(price);
    }

    public static OrderItem create(UUID foodItemId, String name, Integer quantity, BigDecimal price) {
        return new OrderItem(foodItemId, name, quantity, price);
    }

    private int validateQuantity(Integer quantity) {
        Objects.requireNonNull(quantity);

        if (quantity <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero");
        }

        return quantity;
    }

    public Money subtotal() {
        return price.multiply(quantity);
    }

    public UUID getFoodItemId() {
        return foodItemId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }
}
