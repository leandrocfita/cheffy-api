package br.com.fiap.cheffy.presentation.mapper;

import br.com.fiap.cheffy.application.user.dto.UserCommandPort;
import br.com.fiap.cheffy.presentation.dto.UserUpdatePasswordDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserWebMapperPasswordTest {

    private UserWebMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserWebMapper();
    }

    @Test
    void shouldMapUserUpdatePasswordDTOToCommand() {
        UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO("NewPassword@123");
        
        UserCommandPort command = mapper.toCommand(dto);
        
        assertNotNull(command);
        assertEquals("NewPassword@123", command.password());
        assertNull(command.name());
        assertNull(command.email());
        assertNull(command.login());
        assertNull(command.address());
    }

    @Test
    void shouldMapUserUpdatePasswordDTOWithDifferentPassword() {
        UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO("AnotherPass@456");
        
        UserCommandPort command = mapper.toCommand(dto);
        
        assertNotNull(command);
        assertEquals("AnotherPass@456", command.password());
    }

    @Test
    void shouldMapUserUpdatePasswordDTOWithLongPassword() {
        String longPassword = "ThisIsAVeryLongPassword@123WithManyCharacters";
        UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO(longPassword);
        
        UserCommandPort command = mapper.toCommand(dto);
        
        assertNotNull(command);
        assertEquals(longPassword, command.password());
    }

    @Test
    void shouldMapUserUpdatePasswordDTOWithSpecialCharacters() {
        String specialPassword = "P@ssw0rd!#$%&*()";
        UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO(specialPassword);
        
        UserCommandPort command = mapper.toCommand(dto);
        
        assertNotNull(command);
        assertEquals(specialPassword, command.password());
    }

    @Test
    void shouldAlwaysSetOtherFieldsToNull() {
        UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO("TestPassword@123");
        
        UserCommandPort command = mapper.toCommand(dto);
        
        assertNull(command.name());
        assertNull(command.email());
        assertNull(command.login());
        assertNull(command.address());
    }
}
