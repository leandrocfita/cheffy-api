//package br.com.fiap.cheffy.application.user.mapper;
//
//import br.com.fiap.cheffy.application.address.mapper.AddressQueryMapper;
//import br.com.fiap.cheffy.application.user.dto.AddressQueryPort;
//import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
//import br.com.fiap.cheffy.domain.profile.entity.Profile;
//import br.com.fiap.cheffy.domain.user.entity.Address;
//import br.com.fiap.cheffy.domain.user.entity.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class UserQueryMapperTest {
//
//    private UserQueryMapper mapper;
//    private AddressQueryMapper addresMapper;
//
//    @BeforeEach
//    void setUp() {
//        addresMapper = new AddressQueryMapper();
//        mapper = new UserQueryMapper(addresMapper);
//    }
//
//    @Test
//    void shouldMapUserToQuery() {
//        User user = new User(UUID.randomUUID(), "John", "john@email.com", "john", "pass", true);
//        user.addProfile(Profile.create(1L, "CLIENT"));
//        user.addAddress(new Address(1L, "Street", 123, "City", "12345678", "Neighborhood", "SP", null, true));
//
//        UserQueryPort query = mapper.toQuery(user);
//
//        assertEquals("John", query.name());
//        assertEquals("john@email.com", query.email());
//        assertEquals("john", query.login());
//        assertEquals(1, query.profileType().size());
//        assertEquals(1, query.addresses().size());
//    }
//
//    @Test
//    void shouldMapMultipleProfiles() {
//        User user = new User(UUID.randomUUID(), "John", "john@email.com", "john", "pass", true);
//        user.addProfile(Profile.create(1L, "CLIENT"));
//        user.addProfile(Profile.create(2L, "OWNER"));
//
//        UserQueryPort query = mapper.toQuery(user);
//
//        assertEquals(2, query.profileType().size());
//    }
//
//    @Test
//    void shouldMapMultipleAddresses() {
//        User user = new User(UUID.randomUUID(), "John", "john@email.com", "john", "pass", true);
//        user.addAddress(new Address(1L, "Street 1", 123, "City", "12345678", "Neighborhood", "SP", null, true));
//        user.addAddress(new Address(2L, "Street 2", 456, "City", "87654321", "Neighborhood", "RJ", null, false));
//
//        UserQueryPort query = mapper.toQuery(user);
//
//        assertEquals(2, query.addresses().size());
//    }
//
//    @Test
//    void shouldMapAddressWithAllFields() {
//        User user = new User(UUID.randomUUID(), "John", "john@email.com", "john", "pass", true);
//        Address address = new Address(1L, "Main St", 100, "São Paulo", "01000000", "Centro", "SP", "Apt 10", true);
//        user.addAddress(address);
//
//        UserQueryPort query = mapper.toQuery(user);
//
//        AddressQueryPort addressQuery = query.addresses().iterator().next();
//        assertEquals("Main St", addressQuery.streetName());
//        assertEquals(100, addressQuery.number());
//        assertEquals("São Paulo", addressQuery.city());
//        assertEquals("01000000", addressQuery.postalCode());
//        assertEquals("Centro", addressQuery.neighborhood());
//        assertEquals("SP", addressQuery.stateProvince());
//        assertEquals("Apt 10", addressQuery.addressLine());
//        assertTrue(addressQuery.main());
//    }
//
//    @Test
//    void shouldMapUserWithNoAddresses() {
//        User user = new User(UUID.randomUUID(), "John", "john@email.com", "john", "pass", true);
//        user.addProfile(Profile.create(1L, "CLIENT"));
//
//        UserQueryPort query = mapper.toQuery(user);
//
//        assertTrue(query.addresses().isEmpty());
//    }
//
//    @Test
//    void shouldMapUserWithNoProfiles() {
//        User user = new User(UUID.randomUUID(), "John", "john@email.com", "john", "pass", true);
//
//        UserQueryPort query = mapper.toQuery(user);
//
//        assertTrue(query.profileType().isEmpty());
//    }
//}
