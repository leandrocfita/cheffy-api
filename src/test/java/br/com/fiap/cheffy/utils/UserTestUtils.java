package br.com.fiap.cheffy.utils;

import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.user.entity.AuthStatus;
import br.com.fiap.cheffy.domain.user.entity.User;

import java.util.UUID;

public class UserTestUtils {

    public static User createClientUserDomainEntity(){
        return User.create("teste", "email@teste.com", Profile.create(1L, ProfileType.CLIENT.getType()));
    }
    public static User createOwnerUserDomainEntity(){
        return User.create("teste", "email@teste.com", Profile.create(2L, ProfileType.OWNER.getType()));
    }

    public static User createAFullUserEntity(){
        return new User(UUID.randomUUID(), "John", "john@email.com", "some-auth-id", true, AuthStatus.CONFIRMED);
    }

}
