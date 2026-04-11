package br.com.fiap.cheffy.domain.user.entity;

import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.user.exception.AddressNotFoundException;
import br.com.fiap.cheffy.domain.user.exception.InvalidPasswordException;
import br.com.fiap.cheffy.domain.user.exception.UserOperationNotAllowedException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.*;

public class User {

    private UUID id;
    private String name;
    private String email;

    private String authId;
    private boolean active;

    private Set<Profile> profiles = new HashSet<>();
    private Set<Address> addresses = new HashSet<>();

    protected User() {}

    public User(UUID id, String name, String email, String authId, boolean active) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.email = Objects.requireNonNull(email);
        this.active = active;
        this.authId = authId;
    }

    /*Factory Method*/
    public static User create(
            String name,
            String email,
            Profile profile,
            String authId
    ) {
        User user = new User(
                null,
                name,
                email,
                authId,
                true
        );

        user.addProfile(profile);
        return user;
    }

    public void patch(
            String name,
            String email,
            String login
    ) {
        if (name != null && !name.isEmpty()) this.name = name;
        if (email != null && !email.isEmpty()) this.email = email;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public boolean isActive() {
        return active;
    }

    /* Address behavior */

    public void updateAddress(
            Long id,
            String streetName,
            Integer number,
            String city,
            String postalCode,
            String neighborhood,
            String stateProvince,
            String addressLine,
            Boolean main
    ){
        Address address = findAddressByIdOrFail(id);

        if(address.isMain() && Boolean.FALSE.equals(main)) {
            throw new UserOperationNotAllowedException(
                    USER_MUST_HAVE_AT_LEAST_ONE_ADDRESS
            );
        }

        address.patch(
                streetName,
                number,
                city,
                postalCode,
                neighborhood,
                stateProvince,
                addressLine,
                main);

        if (Boolean.TRUE.equals(address.isMain())) {
            setMainAddress(address);
        }

    }

    public Address findAddressByIdOrFail(Long addressId) {
        return addresses.stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new AddressNotFoundException(
                        ADDRESS_NOT_FOUND_EXCEPTION,
                        addressId
                ));
    }

    public void addAddress(Address address) {
        Objects.requireNonNull(address);

        if (Boolean.TRUE.equals(address.isMain())) {
            unsetMainAddress();
        }

        address.attachTo(this);
        addresses.add(address);
    }

    public void setMainAddress(Address address) {
        validateAddressOwnership(address);
        unsetMainAddress();
        address.setMain(true);
    }

    public void removeAddress(Address address) {
        validateAddressOwnership(address);

        boolean wasMain = Boolean.TRUE.equals(address.isMain());
        addresses.remove(address);
        address.detach();

        if (addresses.isEmpty()) {
            throw new UserOperationNotAllowedException(
                    USER_MUST_HAVE_AT_LEAST_ONE_ADDRESS
            );
        }

        if (wasMain) {
            addresses.iterator().next().setMain(true);
        }
    }

    private void unsetMainAddress() {
        addresses.forEach(a -> a.setMain(false));
    }

    private void validateAddressOwnership(Address address) {
        if (!addresses.contains(address)) {
            throw new AddressNotFoundException(
                    ADDRESS_NOT_FOUND_EXCEPTION,
                    address.getId());
        }
    }

    /* Profile behavior */

    public void addProfile(Profile profile) {
        if(!profiles.contains(profile)) {
            profiles.add(profile);
        }
    }

    public void removeProfile(Profile profile) {
        if(profiles.contains(profile)) {
            profiles.remove(profile);
        }
    }


    //Getters
    public String getAuthId() {
        return authId;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Set<Address> getAddresses() {
        return Set.copyOf(addresses);
    }

    public Set<Profile> getProfiles() {
        return Set.copyOf(profiles);
    }



}
