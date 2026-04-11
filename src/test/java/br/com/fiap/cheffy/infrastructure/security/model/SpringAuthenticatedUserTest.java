//package br.com.fiap.cheffy.infrastructure.security.model;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import java.util.Set;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class SpringAuthenticatedUserTest {
//
//    @Test
//    void shouldCreateSpringAuthenticatedUser() {
//        UUID id = UUID.randomUUID();
//        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
//
//        SpringAuthenticatedUser user = new SpringAuthenticatedUser(id, "username", "password", authorities, true);
//
//        assertEquals(id, user.getId());
//        assertEquals("username", user.getUsername());
//        assertEquals("password", user.getPassword());
//        assertEquals(authorities, user.getAuthorities());
//    }
//
//    @Test
//    void shouldReturnTrueForAccountNonExpired() {
//        SpringAuthenticatedUser user = new SpringAuthenticatedUser(
//            UUID.randomUUID(), "user", "pass", Set.of(), true
//        );
//
//        assertTrue(user.isAccountNonExpired());
//    }
//
//    @Test
//    void shouldReturnTrueForAccountNonLocked() {
//        SpringAuthenticatedUser user = new SpringAuthenticatedUser(
//            UUID.randomUUID(), "user", "pass", Set.of(), true
//        );
//
//        assertTrue(user.isAccountNonLocked());
//    }
//
//    @Test
//    void shouldReturnTrueForCredentialsNonExpired() {
//        SpringAuthenticatedUser user = new SpringAuthenticatedUser(
//            UUID.randomUUID(), "user", "pass", Set.of(), true
//        );
//
//        assertTrue(user.isCredentialsNonExpired());
//    }
//
//    @Test
//    void shouldReturnTrueForEnabled() {
//        SpringAuthenticatedUser user = new SpringAuthenticatedUser(
//            UUID.randomUUID(), "user", "pass", Set.of(), true
//        );
//
//        assertTrue(user.isEnabled());
//    }
//
//    @Test
//    void shouldReturnFalseForDisabledUser() {
//        SpringAuthenticatedUser user = new SpringAuthenticatedUser(
//            UUID.randomUUID(), "user", "pass", Set.of(), false
//        );
//
//        assertFalse(user.isEnabled());
//    }
//
//    @Test
//    void shouldHandleMultipleAuthorities() {
//        Set<GrantedAuthority> authorities = Set.of(
//            new SimpleGrantedAuthority("ROLE_USER"),
//            new SimpleGrantedAuthority("ROLE_ADMIN")
//        );
//
//        SpringAuthenticatedUser user = new SpringAuthenticatedUser(
//            UUID.randomUUID(), "admin", "pass", authorities, true
//        );
//
//        assertEquals(2, user.getAuthorities().size());
//    }
//
//    @Test
//    void shouldUseGettersFromLombok() {
//        UUID id = UUID.randomUUID();
//        SpringAuthenticatedUser user = new SpringAuthenticatedUser(id, "user", "pass", Set.of(), true);
//
//        assertNotNull(user.getId());
//        assertNotNull(user.getUsername());
//        assertNotNull(user.getPassword());
//        assertNotNull(user.getAuthorities());
//    }
//
//    @Test
//    void equalsAndHashCodeWork() {
//        UUID id = UUID.randomUUID();
//        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
//
//        SpringAuthenticatedUser user1 = new SpringAuthenticatedUser(id, "user", "pass", authorities, true);
//        SpringAuthenticatedUser user2 = new SpringAuthenticatedUser(id, "user", "pass", authorities, true);
//        SpringAuthenticatedUser user3 = new SpringAuthenticatedUser(UUID.randomUUID(), "other", "pass", authorities, true);
//
//        assertEquals(user1, user2);
//        assertNotEquals(user1, user3);
//        assertEquals(user1.hashCode(), user2.hashCode());
//    }
//
//    @Test
//    void toStringWorks() {
//        UUID id = UUID.randomUUID();
//        SpringAuthenticatedUser user = new SpringAuthenticatedUser(id, "user", "pass", Set.of(), true);
//
//        String toString = user.toString();
//        assertNotNull(toString);
//        assertTrue(toString.contains("SpringAuthenticatedUser"));
//    }
//
//    @Test
//    void equalsWithNull() {
//        UUID id = UUID.randomUUID();
//        SpringAuthenticatedUser user = new SpringAuthenticatedUser(id, "user", "pass", Set.of(), true);
//
//        assertNotEquals(user, null);
//    }
//
//    @Test
//    void equalsWithDifferentClass() {
//        UUID id = UUID.randomUUID();
//        SpringAuthenticatedUser user = new SpringAuthenticatedUser(id, "user", "pass", Set.of(), true);
//
//        assertNotEquals(user, "string");
//    }
//
//    @Test
//    void equalsWithSameObject() {
//        UUID id = UUID.randomUUID();
//        SpringAuthenticatedUser user = new SpringAuthenticatedUser(id, "user", "pass", Set.of(), true);
//
//        assertEquals(user, user);
//    }
//
//    @Test
//    void equalsWithDifferentId() {
//        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
//        SpringAuthenticatedUser user1 = new SpringAuthenticatedUser(UUID.randomUUID(), "user", "pass", authorities, true);
//        SpringAuthenticatedUser user2 = new SpringAuthenticatedUser(UUID.randomUUID(), "user", "pass", authorities, true);
//
//        assertNotEquals(user1, user2);
//    }
//
//    @Test
//    void equalsWithDifferentUsername() {
//        UUID id = UUID.randomUUID();
//        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
//        SpringAuthenticatedUser user1 = new SpringAuthenticatedUser(id, "user1", "pass", authorities, true);
//        SpringAuthenticatedUser user2 = new SpringAuthenticatedUser(id, "user2", "pass", authorities, true);
//
//        assertNotEquals(user1, user2);
//    }
//
//    @Test
//    void equalsWithDifferentPassword() {
//        UUID id = UUID.randomUUID();
//        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
//        SpringAuthenticatedUser user1 = new SpringAuthenticatedUser(id, "user", "pass1", authorities, true);
//        SpringAuthenticatedUser user2 = new SpringAuthenticatedUser(id, "user", "pass2", authorities, true);
//
//        assertNotEquals(user1, user2);
//    }
//
//    @Test
//    void equalsWithDifferentAuthorities() {
//        UUID id = UUID.randomUUID();
//        SpringAuthenticatedUser user1 = new SpringAuthenticatedUser(id, "user", "pass", Set.of(new SimpleGrantedAuthority("ROLE_USER")), true);
//        SpringAuthenticatedUser user2 = new SpringAuthenticatedUser(id, "user", "pass", Set.of(new SimpleGrantedAuthority("ROLE_ADMIN")), true);
//
//        assertNotEquals(user1, user2);
//    }
//
//    @Test
//    void hashCodeConsistency() {
//        UUID id = UUID.randomUUID();
//        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
//        SpringAuthenticatedUser user = new SpringAuthenticatedUser(id, "user", "pass", authorities, true);
//
//        int hash1 = user.hashCode();
//        int hash2 = user.hashCode();
//
//        assertEquals(hash1, hash2);
//    }
//}
