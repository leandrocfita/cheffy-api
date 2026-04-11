package br.com.fiap.cheffy.domain.restaurant.entity;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Menu {

    private final Set<FoodItem> items = new HashSet<>();
    private boolean active = true;

    public Menu(Set<FoodItem> items) {
        this.items.addAll(items);
    }

    void addItem(FoodItem item) {
        if (!active) {
            throw new IllegalStateException("Cannot add items to inactive menu");
        }
        items.add(item);

    }

    public FoodItem removeItem(UUID id) {

        FoodItem item = items.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Food item not found"));

        items.remove(item);

        return item;
    }

    Set<FoodItem> availableItems() {
        if (!active) {
            return Set.of();
        }

        return items.stream()
                .filter(FoodItem::isAvailable)
                .collect(Collectors.toUnmodifiableSet());
    }

    void deactivate() {
        this.active = false;
        items.forEach(FoodItem::deactivate);
    }

    boolean hasActiveItems() {

        return items != null
                && !items.isEmpty()
                && items.stream().anyMatch(FoodItem::isActive);
    }

    public Set<FoodItem> getItems() {
        return items;
    }
}
