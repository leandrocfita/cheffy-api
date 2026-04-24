//package br.com.fiap.cheffy.infrastructure.security.properties;
//
//import org.junit.jupiter.api.Test;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class JwtPropertiesTest {
//
//    @Test
//    void setAndGetProperties() {
//        JwtProperties props = new JwtProperties();
//
//        props.setSecret("secret");
//        props.setExpiration(3600000L);
//        props.setIssuer("cheffy");
//
//        assertThat(props.getSecret()).isEqualTo("secret");
//        assertThat(props.getExpiration()).isEqualTo(3600000L);
//        assertThat(props.getIssuer()).isEqualTo("cheffy");
//    }
//}
