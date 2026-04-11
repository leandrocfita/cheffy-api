package br.com.fiap.cheffy.application.profile.usecase;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputPort;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.exception.ProfileAlreadyExistException;
import br.com.fiap.cheffy.domain.profile.port.input.ProfileCreateInput;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

import java.util.logging.Level;
import java.util.logging.Logger;


public class CreateProfileUseCase implements ProfileCreateInput {

    private final Logger logger = Logger.getLogger(CreateProfileUseCase.class.getName());

    private final ProfileRepository profileRepository;

    public CreateProfileUseCase(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }

    public Long create(ProfileInputPort profileInformation) {

        String nameType = profileInformation.name();

        logger.info("Entered in ProfileCreateUseCase");
        
        Profile profileFound = profileRepository.findByType(nameType).orElse(null);

        if (profileFound != null) {
            logger.log(Level.SEVERE, "Profile already exists in database | profile nameType sent: " + nameType);
            throw new ProfileAlreadyExistException(ExceptionsKeys.PROFILE_ALREADY_EXIST_EXCEPTION, nameType);
        }

        Profile profile = createProfileDomain(null,nameType);

        Long savedProfile = profileRepository.save(profile);

        logger.info("End of ProfileCreateUseCase: Profile created complete with Id: " + savedProfile);

        return savedProfile;
    }

    private Profile createProfileDomain(Long id, String profileType){

        return Profile.create(id, profileType);
    }
}
