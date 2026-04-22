package br.com.fiap.cheffy.domain.order.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    @Test
    void createCalculatesTotalAndStartsWithCreatedStatus() {
        OrderItem firstItem = OrderItem.create(UUID.randomUUID(), "Burger", 2, new BigDecimal("15.00"));
        OrderItem secondItem = OrderItem.create(UUID.randomUUID(), "Soda", 1, new BigDecimal("7.50"));

        Order order = Order.create(UUID.randomUUID(), UUID.randomUUID(), List.of(firstItem, secondItem));

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(order.getTotalAmount().value()).isEqualByComparingTo("37.50");
        assertThat(order.getItems()).hasSize(2);
    }

    @Test
    void createThrowsWhenItemListIsEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> Order.create(UUID.randomUUID(), UUID.randomUUID(), List.of()));
    }

    @Test
    void markPaymentPendingAndPaidUpdateStatus() {
        OrderItem item = OrderItem.create(UUID.randomUUID(), "Burger", 1, new BigDecimal("15.00"));
        Order order = Order.create(UUID.randomUUID(), UUID.randomUUID(), List.of(item));

        order.markPaymentPending();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAYMENT_PENDING);

        order.markAsPaid();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
    }
}
