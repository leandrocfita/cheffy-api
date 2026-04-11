package br.com.fiap.cheffy.application.profile.usecase;

import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import br.com.fiap.cheffy.application.profile.mapper.ProfileQueryMapper;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.port.input.ListAllProfilesInput;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ListAllProfilesUseCase implements ListAllProfilesInput {

    private final ProfileRepository profileRepository;
    private final ProfileQueryMapper mapper;

    public ListAllProfilesUseCase(ProfileRepository profileRepository, ProfileQueryMapper mapper) {
        this.profileRepository = profileRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<ProfileQueryPort> execute(PageRequest pageRequest) {
        PageResult<Profile> profilePage = profileRepository.findAll(pageRequest);

        List<ProfileQueryPort> mappedContent = profilePage.content().stream()
                .map(mapper::toQuery)
                .toList();

        return PageResult.from(profilePage, mappedContent);
    }
}
