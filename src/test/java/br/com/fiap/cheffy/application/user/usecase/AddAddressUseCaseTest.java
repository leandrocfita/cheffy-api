package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;
import br.com.fiap.cheffy.application.user.service.UserServiceHelper;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddAddressUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceHelper userServiceHelper;

    @InjectMocks
    private AddAddressUseCase addAdressUseCase;

    @Test
    void executeAddsAddressAndSavesUser() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Jane Doe", "jane@example.com", "janed", "Password1!Strong", true);
        AddressCommandPort command = new AddressCommandPort(
                "Main Street",
                123,
                "Sao Paulo",
                "01000000",
                "Centro",
                "SP",
                "Apt 1",
                true
        );

        when(userServiceHelper.getUserOrFail(userId)).thenReturn(user);

        addAdressUseCase.execute(command, userId);

        verify(userRepository).save(user);
        assertThat(user.getAddresses()).hasSize(1);

        Address address = user.getAddresses().iterator().next();
        assertThat(address.getStreetName()).isEqualTo("Main Street");
        assertThat(address.getNumber()).isEqualTo(123);
        assertThat(address.getCity()).isEqualTo("Sao Paulo");
        assertThat(address.getPostalCode()).isEqualTo("01000000");
        assertThat(address.getNeighborhood()).isEqualTo("Centro");
        assertThat(address.getStateProvince()).isEqualTo("SP");
        assertThat(address.getAddressLine()).isEqualTo("Apt 1");
        assertThat(address.isMain()).isTrue();
        assertThat(address.getUser()).isEqualTo(user);
    }

    @Test
    void executeReplacesExistingMainAddressWhenNewMainIsAdded() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "John Doe", "john@example.com", "johnd", "Password1!Strong", true);
        Address existing = Address.create(
                "Old Street",
                10,
                "Sao Paulo",
                "02000000",
                "Centro",
                "SP",
                "House",
                true
        );
        user.addAddress(existing);

        AddressCommandPort command = new AddressCommandPort(
                "New Street",
                200,
                "Sao Paulo",
                "03000000",
                "Centro",
                "SP",
                "Apt 2",
                true
        );

        when(userServiceHelper.getUserOrFail(userId)).thenReturn(user);

        addAdressUseCase.execute(command, userId);

        verify(userRepository).save(user);

        List<Address> addresses = user.getAddresses().stream().toList();
        Address newAddress = addresses.stream()
                .filter(address -> "New Street".equals(address.getStreetName()))
                .findFirst()
                .orElseThrow();

        assertThat(newAddress.isMain()).isTrue();
        assertThat(existing.isMain()).isFalse();
    }
}
