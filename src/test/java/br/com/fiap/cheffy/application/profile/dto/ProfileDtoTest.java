package br.com.fiap.cheffy.application.profile.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfileDtoTest {

    @Test
    void createProfileInputPort() {
        ProfileInputPort dto = new ProfileInputPort("Chef");
        assertThat(dto.name()).isEqualTo("Chef");
    }
}
