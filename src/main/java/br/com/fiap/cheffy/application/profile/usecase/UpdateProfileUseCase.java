package br.com.fiap.cheffy.application.profile.usecase;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputPort;
import br.com.fiap.cheffy.application.profile.service.ProfileServiceHelper;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.exception.ProfileAlreadyExistException;
import br.com.fiap.cheffy.domain.profile.exception.ProfileNotFoundException;
import br.com.fiap.cheffy.domain.profile.port.input.ProfileUpdateInput;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateProfileUseCase implements ProfileUpdateInput {

    private final Logger logger = Logger.getLogger(UpdateProfileUseCase.class.getName());

    private final ProfileRepository profileRepository;
    private final ProfileServiceHelper profileServiceHelper;

    public UpdateProfileUseCase(
            ProfileRepository profileRepository,
            ProfileServiceHelper profileServiceHelper
    )
    {
        this.profileRepository = profileRepository;
        this.profileServiceHelper = profileServiceHelper;
    }

    @Override
    public void updateById(Long id, ProfileInputPort profileInputPort){

        logger.info("Entered in UpdateProfileUseCase: Starting updating flow for profile with id: {" + id + "}");

        String newProfileType = profileInputPort.name();

        Profile profileFound = profileServiceHelper.getProfileOrFail(id);

        profileServiceHelper.validateProfileModification(profileFound);

        Profile profileNameAlreadyExist = profileRepository.findByType(profileInputPort.name()).orElse(null);
        
        if (profileNameAlreadyExist != null){
            String msg = String.format("Profile with name: %s, Already exist in database, aborting the profile type update", profileInputPort.name());
            logger.severe(msg);
            throw new ProfileAlreadyExistException(ExceptionsKeys.PROFILE_ALREADY_EXIST_EXCEPTION, profileInputPort.name());
        }


        profileFound.patch(newProfileType);

        profileRepository.save(profileFound);

        logger.info("End of UpdateProfileUseCase: Profile updated complete");


    }

    @Override
    public void updateByName(String nameType, ProfileInputPort profileInputPort){

        logger.info("Entered in UpdateProfileUseCase: Starting updating flow for profile with name: {" + nameType + "}");

        String newProfileType = profileInputPort.name();

        Profile profileFoundById = profileRepository.findByType(nameType).orElse(null);

        if (profileFoundById == null) {
            logger.log(Level.WARNING, "Profile with nameType {0} not found, aborting the profile type update", nameType);
            throw  new ProfileNotFoundException(ExceptionsKeys.PROFILE_NOT_FOUND_EXCEPTION, nameType);
        }
        

        profileFoundById.patch(newProfileType);

        profileRepository.save(profileFoundById);

        logger.info("End of UpdateProfileUseCase: Profile updated complete");

    }
}
