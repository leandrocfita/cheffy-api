package br.com.fiap.cheffy.infrastructure.security.adapter;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class BCryptPasswordEncoderAdapterTest {

    @Test
    void encodePassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        BCryptPasswordEncoderAdapter adapter = new BCryptPasswordEncoderAdapter(encoder);
        
        String encoded = adapter.encode("password");
        
        assertThat(encoded).isNotEqualTo("password");
        assertThat(encoder.matches("password", encoded)).isTrue();
    }
}
