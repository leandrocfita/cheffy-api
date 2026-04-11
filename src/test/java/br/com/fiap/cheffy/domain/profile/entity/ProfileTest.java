package br.com.fiap.cheffy.domain.profile.entity;

import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.user.exception.UserOperationNotAllowedException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileTest {

    @Test
    void createProfile() {
        Profile profile = Profile.create(1L, ProfileType.CLIENT.getType());
        
        assertThat(profile.getId()).isEqualTo(1L);
        assertThat(profile.getType()).isEqualTo(ProfileType.CLIENT.getType());
    }

    @Test
    void createProfileWithInvalidType() {
        assertThatThrownBy(() -> Profile.create(1L, ""))
                .isInstanceOf(UserOperationNotAllowedException.class)
                .hasMessage("PROFILE_DATA_NOT_VALID");

        assertThatThrownBy(() -> Profile.create(1L, null))
                .isInstanceOf(UserOperationNotAllowedException.class)
                .hasMessage("PROFILE_DATA_NOT_VALID");
    }

    @Test
    void patchProfile(){
        Profile profile = Profile.create(1L, ProfileType.CLIENT.getType());
        profile.patch(ProfileType.OWNER.getType());

        assertThat(profile.getType()).isEqualTo(ProfileType.OWNER.getType());
    }

    @Test
    void patchProfileWithInvalidType(){
        Profile profile = Profile.create(1L, ProfileType.CLIENT.getType());

        assertThatThrownBy(() -> profile.patch(""))
                .isInstanceOf(UserOperationNotAllowedException.class)
                .hasMessage("PROFILE_DATA_NOT_VALID");

        assertThatThrownBy(() -> profile.patch(null))
                .isInstanceOf(UserOperationNotAllowedException.class)
                .hasMessage("PROFILE_DATA_NOT_VALID");
    }

    @Test
    void createProfileThrowsWhenTypeIsNull() {
        assertThrows(UserOperationNotAllowedException.class, () -> Profile.create(1L, null));
    }

    @Test
    void createProfileThrowsWhenTypeIsBlank() {
        assertThrows(UserOperationNotAllowedException.class, () -> Profile.create(1L, ""));
    }
}
