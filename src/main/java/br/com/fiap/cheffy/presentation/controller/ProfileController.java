package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputPort;
import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.profile.port.input.*;
import br.com.fiap.cheffy.presentation.config.swagger.docs.ProfileControllerDocs;
import br.com.fiap.cheffy.presentation.dto.ProfileCreateReponseDto;
import br.com.fiap.cheffy.presentation.dto.ProfileInputDto;
import br.com.fiap.cheffy.presentation.mapper.ProfileWebMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController implements ProfileControllerDocs {

    private final ProfileCreateInput profileCreateInput;
    private final ProfileUpdateInput profileUpdateInput;
    private final FindProfileByInput findProfileByIdInput;
    private final ListAllProfilesInput listAllProfilesInput;
    private final ProfileDeleteInput profileDeleteInput;

    public ProfileController(
            ProfileCreateInput profileCreateInput,
            ProfileUpdateInput profileUpdateInput,
            ListAllProfilesInput listAllProfilesInput,
            FindProfileByInput findProfileByIdInput,
            ProfileDeleteInput profileDeleteInput) {
        this.profileCreateInput = profileCreateInput;
        this.profileUpdateInput = profileUpdateInput;
        this.findProfileByIdInput = findProfileByIdInput;
        this.listAllProfilesInput = listAllProfilesInput;
        this.profileDeleteInput = profileDeleteInput;
    }

    @Override
    @PostMapping("")
    public ResponseEntity<ProfileCreateReponseDto> createProfile(@RequestBody @Valid ProfileInputDto profileInputDto) {
        log.info("HTTP request received to create profile");
        ProfileInputPort profileInputPort = ProfileWebMapper.toProfileInputCommandPort(profileInputDto);
        Long id = profileCreateInput.create(profileInputPort);
        ProfileCreateReponseDto profileCreateReponseDto =
                new ProfileCreateReponseDto(id, profileInputDto.profileNameType(), "Profile created successfully");
        log.info("Profile created successfully [profileId={}]", profileCreateReponseDto.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(profileCreateReponseDto);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProfileById(@PathVariable Long id, @RequestBody @Valid ProfileInputDto profileInputDto) {
        log.info("HTTP request received to update profile [profileId={}]", id);
        ProfileInputPort profileInputPort = ProfileWebMapper.toProfileInputCommandPort(profileInputDto);
        profileUpdateInput.updateById(id, profileInputPort);
        log.info("Profile updated successfully [profileId={}]", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/name/{name}")
    public ResponseEntity<Void> updateProfileByName(@PathVariable String name, @RequestBody @Valid ProfileInputDto profileInputDto) {
        log.info("HTTP request received to update profile by name [name={}]", name);
        ProfileInputPort profileInputPort = ProfileWebMapper.toProfileInputCommandPort(profileInputDto);
        profileUpdateInput.updateByName(name, profileInputPort);
        log.info("Profile updated successfully [name={}]", name);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProfileQueryPort> findProfileById(@PathVariable Long id) {
        try {
            log.info("HTTP request received to find profile by id [profileId={}]", id);
            var profile = findProfileByIdInput.execute(id);
            log.info("Profile found successfully [profileId={}]", id);
            return ResponseEntity.ok(profile);
        } finally {
            MDC.clear();
        }
    }

    @Override
    @GetMapping
    public ResponseEntity<PageResult<ProfileQueryPort>> listAllProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "type") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        log.info("HTTP request received to list all profiles [page={}, size={}, sortBy={}, direction={}]", page, size, sortBy, direction);
        PageRequest.SortDirection sortDirection = direction == Sort.Direction.DESC
                ? PageRequest.SortDirection.DESC
                : PageRequest.SortDirection.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, sortBy, sortDirection);
        PageResult<ProfileQueryPort> profiles = listAllProfilesInput.execute(pageRequest);
        log.info("Profiles found successfully, [{}] profiles were found on page [{}]", profiles.numberOfElements(), page);
        return ResponseEntity.ok(profiles);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        try {
            log.info("HTTP request received to delete profile [profileId={}]", id);
            profileDeleteInput.execute(id);
            log.info("Profile deleted successfully [profileId={}]", id);
            return ResponseEntity.noContent().build();
        } finally {
            MDC.clear();
        }
    }
}
