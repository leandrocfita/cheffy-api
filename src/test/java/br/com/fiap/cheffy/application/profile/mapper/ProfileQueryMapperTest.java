package br.com.fiap.cheffy.application.profile.mapper;

import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileQueryMapperTest {

    private ProfileQueryMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProfileQueryMapper();
    }

    @Test
    void shouldMapProfileToQuery() {
        Profile profile = Profile.create(1L, "CLIENT");

        ProfileQueryPort query = mapper.toQuery(profile);

        assertEquals(1L, query.id());
        assertEquals("CLIENT", query.type());
    }
}