package br.com.fiap.cheffy.domain.profile.entity;

import br.com.fiap.cheffy.domain.user.exception.UserOperationNotAllowedException;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class Profile {

    private final Long id;
    private String type;

    private Profile(Long id, String type) {
        validateProfile(type);
        this.id = id;
        this.type = type;
    }

    public static Profile create(Long id, String profileType){
        return new Profile(id, profileType);
    }

    public Long getId() {
        return id;
    }

    public String getType() {return type;}

    public void patch(String name){
        validateProfile(name);
        this.type = name;
    }

    private void validateProfile(String profileType) throws UserOperationNotAllowedException {

        if (profileType == null || profileType.isBlank()) {
            throw new UserOperationNotAllowedException(ExceptionsKeys.PROFILE_DATA_NOT_VALID);
        }
    }

}
