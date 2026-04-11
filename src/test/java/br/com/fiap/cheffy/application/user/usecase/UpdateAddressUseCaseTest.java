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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateAddressUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceHelper userServiceHelper;

    @InjectMocks
    private UpdateAddressUseCase updateAddressUseCase;

    @Test
    void executeUpdatesAddressAndSavesUser() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Jane Doe", "jane@example.com", "janed", "Password1!Strong", true);
        Address existingMain = new Address(
                2L,
                "Old Street",
                100,
                "Sao Paulo",
                "01000-000",
                "Centro",
                "SP",
                "House",
                true
        );
        Address addressToUpdate = new Address(
                1L,
                "Main Street",
                123,
                "Sao Paulo",
                "02000-000",
                "Centro",
                "SP",
                "Apt 1",
                false
        );
        user.addAddress(existingMain);
        user.addAddress(addressToUpdate);

        AddressCommandPort command = new AddressCommandPort(
                "Updated Street",
                456,
                "Sao Paulo",
                "03000-000",
                "Bela Vista",
                "SP",
                "Apt 10",
                true
        );

        when(userServiceHelper.getUserOrFail(userId)).thenReturn(user);

        updateAddressUseCase.execute(userId, 1L, command);

        verify(userRepository).save(user);

        Address updated = user.getAddresses().stream()
                .filter(address -> Long.valueOf(1L).equals(address.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(updated.getStreetName()).isEqualTo("Updated Street");
        assertThat(updated.getNumber()).isEqualTo(456);
        assertThat(updated.getCity()).isEqualTo("Sao Paulo");
        assertThat(updated.getPostalCode()).isEqualTo("03000-000");
        assertThat(updated.getNeighborhood()).isEqualTo("Bela Vista");
        assertThat(updated.getStateProvince()).isEqualTo("SP");
        assertThat(updated.getAddressLine()).isEqualTo("Apt 10");
        assertThat(updated.isMain()).isTrue();
        assertThat(existingMain.isMain()).isFalse();
    }
}
