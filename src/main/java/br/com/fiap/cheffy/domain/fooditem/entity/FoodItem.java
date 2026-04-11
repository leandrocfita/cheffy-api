package br.com.fiap.cheffy.domain.fooditem.entity;

import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.valueobject.Money;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class FoodItem {

    private final UUID id;

    private String name;
    private String description;
    private Money price;
    private String photoKey;
    private Restaurant restaurant;
    private boolean deliveryAvailable;
    private boolean available;
    private boolean active;

    FoodItem(
            UUID id,
            String name,
            String description,
            BigDecimal price,
            String photoKey,
            boolean deliveryAvailable,
            boolean available
            ) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.price = new Money(Objects.requireNonNull(price));
        this.photoKey = Objects.requireNonNull(photoKey);
        this.deliveryAvailable = deliveryAvailable;
        this.available = available;
        this.active = true;
    }

    FoodItem(
            UUID id,
            String name,
            String description,
            BigDecimal price,
            String photoKey,
            boolean deliveryAvailable,
            boolean available,
            boolean active
    ) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.price = new Money(Objects.requireNonNull(price));
        this.photoKey = Objects.requireNonNull(photoKey);
        this.deliveryAvailable = deliveryAvailable;
        this.available = available;
        this.active = active;
    }


    public static FoodItem create(
            String name,
            String description,
            BigDecimal price,
            String photoKey,
            boolean deliveryAvailable,
            boolean available,
            Restaurant restaurant
    ) {
        FoodItem foodItem = new FoodItem(
                null,
                name,
                description,
                price,
                photoKey,
                deliveryAvailable,
                available
                );

        foodItem.setRestaurant(restaurant);

        return foodItem;
    }

    public static FoodItem reconstitute(
            UUID id,
            String name,
            String description,
            BigDecimal price,
            String photoKey,
            boolean deliveryAvailable,
            boolean available,
            boolean active
    ) {
        return new FoodItem(
                id,
                name,
                description,
                price,
                photoKey,
                deliveryAvailable,
                available,
                active
        );
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void deactivate() {
        this.active = false;
        this.available = false;
        this.deliveryAvailable = false;
    }

    public void reactivate() {
        this.active = true;
        this.available = true;
        this.deliveryAvailable = true;
    }

    public void updateAvailability(Boolean available, Boolean deliveryAvailable) {
        if (available != null) {
            this.available = available;
            if (!available) {
                this.deliveryAvailable = false;
            }
        }
        if (deliveryAvailable != null) {
            this.deliveryAvailable = deliveryAvailable;
        }
    }

    public boolean isAvailable() {
        return available && active;
    }

    public void makeUnavailable() {
        this.available = false;
    }

    public boolean isActive() {
        return active;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Money getPrice() {
        return price;
    }

    public String getPhotoKey() {
        return photoKey;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public boolean isDeliveryAvailable() {
        return deliveryAvailable;
    }

    public void patch(FoodItem patchEntity){

        if(patchEntity.getName() != null && !patchEntity.getName().isBlank()) this.name = patchEntity.getName();

        if(patchEntity.getDescription() != null && !patchEntity.getDescription().isBlank()) this.description = patchEntity.getDescription();

        if(patchEntity.getPhotoKey() != null && !patchEntity.getPhotoKey().isBlank()) this.photoKey = patchEntity.getPhotoKey();

        if (patchEntity.getPrice() != null && !patchEntity.getPrice().isZero()) this.price = patchEntity.getPrice();

        if (patchEntity.isDeliveryAvailable() != this.deliveryAvailable) this.deliveryAvailable = patchEntity.isDeliveryAvailable();

        if (patchEntity.isAvailable() != this.available) this.available = patchEntity.isAvailable();

        if (patchEntity.isActive() != this.active) this.active = patchEntity.isActive();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodItem that = (FoodItem) o;

        if (this.id == null || that.id == null) return false;

        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return (id != null) ? id.hashCode() : System.identityHashCode(this);
    }
}

