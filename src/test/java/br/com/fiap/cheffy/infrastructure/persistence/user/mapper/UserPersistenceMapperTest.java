//package br.com.fiap.cheffy.infrastructure.persistence.user.mapper;
//
//import br.com.fiap.cheffy.domain.profile.ProfileType;
//import br.com.fiap.cheffy.domain.profile.entity.Profile;
//import br.com.fiap.cheffy.domain.user.entity.Address;
//import br.com.fiap.cheffy.domain.user.entity.User;
//import br.com.fiap.cheffy.infrastructure.persistence.address.mapper.AddressPersistenceMapper;
//import br.com.fiap.cheffy.infrastructure.persistence.profile.entity.ProfileJpaEntity;
//import br.com.fiap.cheffy.infrastructure.persistence.profile.mapper.ProfilePersistenceMapper;
//import br.com.fiap.cheffy.infrastructure.persistence.address.entity.AddressJpaEntity;
//import br.com.fiap.cheffy.infrastructure.persistence.user.entity.UserJpaEntity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class UserPersistenceMapperTest {
//
//    @Mock
//    private ProfilePersistenceMapper profileMapper;
//
//    private AddressPersistenceMapper addressPersistenceMapper;
//
//    private UserPersistenceMapper mapper;
//
//    @BeforeEach
//    void setup() {
//        addressPersistenceMapper = new AddressPersistenceMapper();
//        mapper = new UserPersistenceMapper(
//                profileMapper,
//                addressPersistenceMapper
//        );
//    }
//
//    @Test
//    void toJpaMapsUserToJpaEntity() {
//        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", "pass", true);
//        user.addProfile(Profile.create(1L, "CLIENT"));
//        when(profileMapper.toJpaReference(any())).thenReturn(new ProfileJpaEntity());
//
//        UserJpaEntity result = mapper.toJpa(user);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getName()).isEqualTo("Name");
//        assertThat(result.getEmail()).isEqualTo("email@test.com");
//    }
//
//    @Test
//    void toJpaMapsUserWithAddresses() {
//        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", "pass", true);
//        user.addProfile(Profile.create(1L, "CLIENT"));
//        Address address1 = new Address(1L, "Street", 123, "City", "12345678", "Neighborhood", "SP", null, true);
//        user.addAddress(address1);
//        when(profileMapper.toJpaReference(any())).thenReturn(new ProfileJpaEntity());
//
//        UserJpaEntity result = mapper.toJpa(user);
//
//        assertThat(result.getAddresses()).hasSize(1);
//        AddressJpaEntity address = result.getAddresses().iterator().next();
//        assertThat(address.getStreetName()).isEqualTo("Street");
//        assertThat(address.getNumber()).isEqualTo(123);
//    }
//
//    @Test
//    void toJpaMapsUserWithProfiles() {
//        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", "pass", true);
//        user.addProfile(Profile.create(1L, "CLIENT"));
//        ProfileJpaEntity profileJpa = new ProfileJpaEntity();
//        when(profileMapper.toJpaReference(any())).thenReturn(profileJpa);
//
//        UserJpaEntity result = mapper.toJpa(user);
//
//        assertThat(result.getProfiles()).hasSize(1);
//    }
//
//    @Test
//    void toDomainMapsJpaEntityToUser() {
//        UserJpaEntity entity = new UserJpaEntity();
//        entity.setId(UUID.randomUUID());
//        entity.setName("Name");
//        entity.setEmail("email@test.com");
//        entity.setLogin("login");
//        entity.setPassword("pass");
//        entity.setProfiles(new HashSet<>());
//        entity.setAddresses(new HashSet<>());
//
//        User result = mapper.toDomain(entity);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getName()).isEqualTo("Name");
//        assertThat(result.getEmail()).isEqualTo("email@test.com");
//    }
//
//    @Test
//    void toDomainMapsJpaEntityWithAddresses() {
//        UserJpaEntity entity = new UserJpaEntity();
//        entity.setId(UUID.randomUUID());
//        entity.setName("Name");
//        entity.setEmail("email@test.com");
//        entity.setLogin("login");
//        entity.setPassword("pass");
//        entity.setProfiles(new HashSet<>());
//
//        AddressJpaEntity addressJpa = new AddressJpaEntity();
//        addressJpa.setId(1L);
//        addressJpa.setStreetName("Street");
//        addressJpa.setNumber(123);
//        addressJpa.setCity("City");
//        addressJpa.setPostalCode("12345678");
//        addressJpa.setNeighborhood("Neighborhood");
//        addressJpa.setStateProvince("SP");
//        addressJpa.setMain(true);
//        entity.setAddresses(Set.of(addressJpa));
//
//        User result = mapper.toDomain(entity);
//
//        assertThat(result.getAddresses()).hasSize(1);
//        Address address = result.getAddresses().iterator().next();
//        assertThat(address.getStreetName()).isEqualTo("Street");
//    }
//
//    @Test
//    void toDomainMapsJpaEntityWithProfiles() {
//        UserJpaEntity entity = new UserJpaEntity();
//        entity.setId(UUID.randomUUID());
//        entity.setName("Name");
//        entity.setEmail("email@test.com");
//        entity.setLogin("login");
//        entity.setPassword("pass");
//        entity.setAddresses(new HashSet<>());
//
//        ProfileJpaEntity profileJpa = new ProfileJpaEntity();
//        profileJpa.setType("CLIENT");
//        entity.setProfiles(Set.of(profileJpa));
//
//        Profile profile = Profile.create(1L, "CLIENT");
//        when(profileMapper.toDomain(any())).thenReturn(profile);
//
//        User result = mapper.toDomain(entity);
//
//        assertThat(result.getProfiles()).hasSize(1);
//    }
//}
