package br.com.fiap.cheffy.domain.restaurant.entity;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantOperationNotAllowedException;
import br.com.fiap.cheffy.domain.restaurant.valueobject.WorkingHours;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.User;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.*;


public class Restaurant {

    private final UUID id;
    private String name;
    private String cnpj;
    private WorkingHours workingHours;
    private ZoneId zoneId;
    private String culinary;
    private Address address;

    private User owner;

    private Menu menu;
    private boolean active;

    protected Restaurant(
            UUID id,
            String name,
            String cnpj,
            String culinary,
            ZoneId zoneId,
            WorkingHours workingHours
            ) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.cnpj = Objects.requireNonNull(cnpj);
        this.culinary = Objects.requireNonNull(culinary);
        this.workingHours = Objects.requireNonNull(workingHours);
        this.zoneId = Objects.requireNonNull(zoneId);
        this.menu = new Menu(new HashSet<>());
        this.active = true;
    }

    public static Restaurant create24h(
            String name,
            String cnpj,
            String culinary,
            ZoneId zoneId,
            User user
    ) {
        return Restaurant.createRestaurant(
                name,
                cnpj,
                culinary,
                zoneId,
                WorkingHours.open24Hours(),
                user
        );
    }

    public static Restaurant createWithWorkingHours(
            String name,
            String cnpj,
            String culinary,
            ZoneId zoneId,
            LocalTime opening,
            LocalTime closing,
            User user
    ) {
        return Restaurant.createRestaurant(
                name,
                cnpj,
                culinary,
                zoneId,
                WorkingHours.of(opening, closing),
                user
        );
    }

    private static Restaurant createRestaurant(
            String name,
            String cnpj,
            String culinary,
            ZoneId zoneId,
            WorkingHours workingHours,
            User user
    ){
        Restaurant restaurant = new Restaurant(
                null,
                name,
                cnpj,
                culinary,
                zoneId,
                workingHours
        );
        restaurant.setOwner(user);
        return restaurant;
    }

    public void deactivate() {
        if (!this.isActive()) {
            throw new RestaurantOperationNotAllowedException(RESTAURANT_IS_ALREADY_INACTIVE);
        }
        this.active = false;
    }

    public void reactivate() {
        if (this.isActive()) {
            throw new RestaurantOperationNotAllowedException(RESTAURANT_IS_ALREADY_ACTIVE);
        }
        this.active = true;
    }

    public void patch(String name, String culinary, ZoneId zoneId, WorkingHours workingHours) {
        if (!this.active) {
            throw new RestaurantOperationNotAllowedException(RESTAURANT_IS_INACTIVE_CANNOT_UPDATE);
        }
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        if (culinary != null && !culinary.isBlank()) {
            this.culinary = culinary;
        }
        if (zoneId != null) {
            this.zoneId = zoneId;
        }
        if (workingHours != null) {
            this.workingHours = workingHours;
        }
    }

    public boolean isOwnedByUser (UUID userId) {
        return this.owner.getId().equals(userId)
                && owner.getProfiles().stream()
                .anyMatch(profile -> ProfileType.OWNER.getType().equals(profile.getType()))
                && owner.isActive();
    }

    //reconstitute
    protected Restaurant(
            UUID id,
            String name,
            String cnpj,
            String culinary,
            ZoneId zoneId,
            WorkingHours workingHours,
            boolean active,
            Address address,
            User owner,
            Menu menu
    ) {
        this.id = id;
        this.name = name;
        this.cnpj = cnpj;
        this.culinary = culinary;
        this.zoneId = zoneId;
        this.workingHours = workingHours;
        this.active = active;
        this.address = address;
        this.owner = owner;
        this.menu = menu;
    }

    public static Restaurant reconstitute(
            UUID id,
            String name,
            String cnpj,
            String culinary,
            ZoneId zoneId,
            LocalTime openingTime,
            LocalTime closingTime,
            boolean open24hours,
            boolean active,
            Address address,
            User owner,
            Menu menu
    ) {


        return new Restaurant(
                id,
                name,
                cnpj,
                culinary,
                zoneId,
                WorkingHours.reconstitute(openingTime, closingTime, open24hours),
                active,
                address,
                owner,
                menu);
    }

    public boolean isOpenAt(Instant instant) {

        if (!active) {
            return false;
        }

        LocalTime localTime =
                instant.atZone(zoneId).toLocalTime();

        return workingHours.isOpenAt(localTime);
    }

    public boolean isOpenNow() {
        return isOpenAt(Instant.now());
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public void addAddress(Address address) {
        this.address = address;
    }

    public void addFoodItem(FoodItem item) {

        item.setRestaurant(this);
        menu.addItem(item);
    }

    public void removeFoodItem(UUID itemId) {

        FoodItem removed = menu.removeItem(itemId);
        removed.setRestaurant(null);
    }

    public Menu getMenu() {

        return menu;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCnpj() {
        return cnpj;
    }

    public LocalTime getOpeningTime() {
        return workingHours.getOpeningTime();
    }

    public LocalTime getClosingTime() {
        return workingHours.getClosingTime();
    }

    public ZoneId getZoneId() {
        return zoneId;
    }

    public boolean isOpen24hours() {
        return workingHours.isOpen24Hours();
    }

    public String getCulinary() {
        return culinary;
    }

    public Address getAddress() {
        return address;
    }

    public User getOwner() {
        return owner;
    }

    public boolean isActive() {
        return active;
    }
}

