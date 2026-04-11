package br.com.fiap.cheffy.domain.user.entity;

import br.com.fiap.cheffy.domain.user.exception.InvalidPostalCodeException;

import java.util.Objects;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.INVALID_POSTAL_CODE_MSG;

public class Address {
    private static final int MIN_POSTAL_CODE_LENGTH = 8; //TODO parametrizar

    private Long id;
    private String streetName;
    private Integer number;
    private String city;
    private String postalCode;
    private String neighborhood;
    private String stateProvince;
    private String addressLine;

    private boolean main;
    private User user;

    protected Address() {}

    public Address(
            Long id,
            String streetName,
            Integer number,
            String city,
            String postalCode,
            String neighborhood,
            String stateProvince,
            String addressLine,
            boolean main) {

        this.id = id;
        this.streetName = Objects.requireNonNull(streetName);
        this.number = Objects.requireNonNull(number);
        this.city = Objects.requireNonNull(city);
        this.postalCode = Objects.requireNonNull(postalCode);
        this.neighborhood = Objects.requireNonNull(neighborhood);
        this.stateProvince = Objects.requireNonNull(stateProvince);
        this.addressLine = addressLine;
        this.main = main;
    }

    public static Address create(
            String streetName,
            Integer number,
            String city,
            String postalCode,
            String neighborhood,
            String stateProvince,
            String addressLine,
            boolean isMain
    ) {
        validatePostalCode(postalCode);

        return new Address(
                null,
                streetName,
                number,
                city,
                postalCode,
                neighborhood,
                stateProvince,
                addressLine,
                isMain
        );
    }

    public void patch(
            String streetName,
            Integer number,
            String city,
            String postalCode,
            String neighborhood,
            String stateProvince,
            String addressLine,
            Boolean main
    ) {
        if (streetName != null) this.streetName = streetName;
        if (number != null) this.number = number;
        if (city != null) this.city = city;
        if (postalCode != null) this.postalCode = postalCode;
        if (neighborhood != null) this.neighborhood = neighborhood;
        if (stateProvince != null) this.stateProvince = stateProvince;
        if (addressLine != null) this.addressLine = addressLine;
        if (main != null) this.main = main;
    }

    public static void validatePostalCode(String postalCode) {
        boolean allDigits = postalCode.chars().allMatch(Character::isDigit);

        if (!(postalCode.length() == MIN_POSTAL_CODE_LENGTH
                && allDigits)) {
            throw new InvalidPostalCodeException(
                    INVALID_POSTAL_CODE_MSG, MIN_POSTAL_CODE_LENGTH);
        }
    }

    /* Relationship control */

    void attachTo(User user) {
        this.user = user;
    }

    void detach() {
        this.user = null;
    }

    /* State changes */

    void setMain(boolean main) {
        this.main = main;
    }

    //Getters

    public Long getId() {
        return id;
    }

    public boolean isMain() {
        return main;
    }

    public User getUser() {
        return user;
    }

    public String getStreetName() {
        return streetName;
    }

    public Integer getNumber() {
        return number;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public String getAddressLine() {
        return addressLine;
    }
}