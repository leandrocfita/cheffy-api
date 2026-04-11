package br.com.fiap.cheffy.domain.profile;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileTypeTest {

    @Test
    void clientProfileType() {
        assertThat(ProfileType.CLIENT.getType()).isEqualTo("CLIENT");
    }

    @Test
    void ownerProfileType() {
        assertThat(ProfileType.OWNER.getType()).isEqualTo("OWNER");
    }
}
