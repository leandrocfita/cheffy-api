package br.com.fiap.cheffy.presentation.interfaces;

import java.math.BigDecimal;

public interface FoodItemRequest {
    String name();
    String description();
    BigDecimal price();
    String photoKey();
    boolean deliveryAvailable();
    boolean available();
    boolean active();
}
