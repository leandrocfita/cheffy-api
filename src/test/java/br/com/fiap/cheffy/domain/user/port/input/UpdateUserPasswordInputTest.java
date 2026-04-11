package br.com.fiap.cheffy.domain.user.port.input;

import br.com.fiap.cheffy.application.user.dto.UserCommandPort;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UpdateUserPasswordInputTest {

    @Test
    void shouldBeAnInterface() {
        assertTrue(UpdateUserPasswordInput.class.isInterface());
    }

    @Test
    void shouldHaveExecuteMethod() throws NoSuchMethodException {
        var method = UpdateUserPasswordInput.class.getMethod("execute", UserCommandPort.class, UUID.class);
        
        assertNotNull(method);
        assertEquals(void.class, method.getReturnType());
        assertEquals(2, method.getParameterCount());
    }

    @Test
    void shouldBeImplementable() {
        UpdateUserPasswordInput implementation = new UpdateUserPasswordInput() {
            @Override
            public void execute(UserCommandPort command, UUID id) {
                assertNotNull(command);
                assertNotNull(id);
            }
        };

        UUID testId = UUID.randomUUID();
        UserCommandPort command = new UserCommandPort(null, null, null, "password", null);
        
        assertDoesNotThrow(() -> implementation.execute(command, testId));
    }

    @Test
    void shouldAcceptNullableFieldsInCommand() {
        UpdateUserPasswordInput implementation = (command, id) -> {
            assertNull(command.name());
            assertNull(command.email());
            assertNull(command.login());
            assertNotNull(command.password());
            assertNull(command.address());
        };

        UUID testId = UUID.randomUUID();
        UserCommandPort command = new UserCommandPort(null, null, null, "ValidPassword@123", null);
        
        assertDoesNotThrow(() -> implementation.execute(command, testId));
    }
}
