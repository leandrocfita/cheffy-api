//package br.com.fiap.cheffy.infrastructure.security.service;
//
//import br.com.fiap.cheffy.domain.profile.ProfileType;
//import br.com.fiap.cheffy.domain.profile.entity.Profile;
//import br.com.fiap.cheffy.domain.user.entity.User;
//import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
//import br.com.fiap.cheffy.infrastructure.security.model.SpringAuthenticatedUser;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class CustomUserDetailsServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private CustomUserDetailsService userDetailsService;
//
//    @Test
//    void loadUserByUsernameReturnsUserDetails() {
//        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", "pass", true);
//        user.addProfile(Profile.create(1L, ProfileType.CLIENT.getType()));
//        when(userRepository.findByLogin("login")).thenReturn(Optional.of(user));
//
//        UserDetails result = userDetailsService.loadUserByUsername("login");
//
//        assertThat(result).isNotNull();
//        assertThat(result.getUsername()).isEqualTo("login");
//        assertThat(result.getPassword()).isEqualTo("pass");
//    }
//
//    @Test
//    void loadUserByUsernameThrowsWhenNotFound() {
//        when(userRepository.findByLogin("unknown")).thenReturn(Optional.empty());
//
//        assertThrows(UsernameNotFoundException.class,
//            () -> userDetailsService.loadUserByUsername("unknown"));
//    }
//
//    @Test
//    void loadUserByIdReturnsUser() {
//        UUID id = UUID.randomUUID();
//        User user = new User(id, "Name", "email@test.com", "login", "pass", true);
//        user.addProfile(Profile.create(1L, ProfileType.CLIENT.getType()));
//        when(userRepository.findById(id)).thenReturn(Optional.of(user));
//
//        SpringAuthenticatedUser result = userDetailsService.loadUserById(id);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo(id);
//    }
//
//    @Test
//    void loadUserByIdThrowsWhenNotFound() {
//        UUID id = UUID.randomUUID();
//        when(userRepository.findById(id)).thenReturn(Optional.empty());
//
//        assertThrows(UsernameNotFoundException.class,
//            () -> userDetailsService.loadUserById(id));
//    }
//}
