package br.com.fiap.cheffy.domain.order.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderItemTest {

    @Test
    void subtotalMultipliesPriceByQuantity() {
        OrderItem item = OrderItem.create(UUID.randomUUID(), "Burger", 2, new BigDecimal("15.00"));

        assertThat(item.subtotal().value()).isEqualByComparingTo("30.00");
    }

    @Test
    void createThrowsWhenQuantityIsZeroOrNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> OrderItem.create(UUID.randomUUID(), "Burger", 0, new BigDecimal("15.00")));

        assertThrows(IllegalArgumentException.class,
                () -> OrderItem.create(UUID.randomUUID(), "Burger", -1, new BigDecimal("15.00")));
    }
}
