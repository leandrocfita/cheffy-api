package br.com.fiap.cheffy.presentation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static org.assertj.core.api.Assertions.assertThat;

class JacksonConfigTest {

    @Test
    void jacksonCustomizerCreatesBean() {
        JacksonConfig config = new JacksonConfig();

        Jackson2ObjectMapperBuilderCustomizer customizer = config.jacksonCustomizer();

        assertThat(customizer).isNotNull();
    }

    @Test
    void jacksonCustomizerConfiguresObjectMapper() {
        JacksonConfig config = new JacksonConfig();
        Jackson2ObjectMapperBuilderCustomizer customizer = config.jacksonCustomizer();
        
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        customizer.customize(builder);
        
        ObjectMapper mapper = builder.build();
        assertThat(mapper).isNotNull();
    }
}
