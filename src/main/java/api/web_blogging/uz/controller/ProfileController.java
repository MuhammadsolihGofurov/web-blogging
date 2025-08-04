package api.web_blogging.uz.controller;

import api.web_blogging.uz.dto.AppResponse;
import api.web_blogging.uz.dto.ProfileDto;
import api.web_blogging.uz.dto.profile.*;
import api.web_blogging.uz.dto.registerDTO;
import api.web_blogging.uz.dto.sms.CodeDTO;
import api.web_blogging.uz.enums.AppLang;
import api.web_blogging.uz.services.ProfileEntityService;
import api.web_blogging.uz.utils.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@Tag(name = "Profile APIs", description = "API list for managing Profile")
@Slf4j
public class ProfileController {

//    private static Logger log = LoggerFactory.getLogger(ProfileController.class);


    @Autowired
    private ProfileEntityService profileEntityService;

    @PutMapping("/detail")
    @Operation(summary = "Update Detail With Name", description = "Api for update detail with name")
    public ResponseEntity<AppResponse<String>> updateDetailWithName(@Valid @RequestBody ProfileDetailUpdateDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLang lang) {
        AppResponse<String> response = profileEntityService.updateProfile(dto, lang);
        log.debug("Debug log message test");
        log.info("Info log message test");
        log.error("Error log message test");
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/profile-photo")
    @Operation(summary = "Update Detail With photo", description = "Api for update detail with photo")
    public ResponseEntity<AppResponse<String>> updateProfilePhoto(@Valid @RequestBody ProfileDetailUpdatePhotoDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLang lang) {
        AppResponse<String> response = profileEntityService.updateProfilePhoto(dto, lang);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/detail/password")
    @Operation(summary = "Update Detail With password", description = "Api for update detail with password")
    public ResponseEntity<AppResponse<String>> updateDetailWithPassword(@Valid @RequestBody ProfileDetailUpdatePasswordDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLang lang) {
        AppResponse<String> response = profileEntityService.updateProfileWithPassword(dto, lang);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/detail/username")
    @Operation(summary = "Update Detail With Username", description = "Api for update detail with username")
    public ResponseEntity<AppResponse<String>> updateDetailWithUsername(@Valid @RequestBody ProfileDetailUpdateUsernameDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLang lang) {
        AppResponse<String> response = profileEntityService.updateProfileWithUsername(dto, lang);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/detail/confirm-username")
    @Operation(summary = "Update Detail With username confirm", description = "Api for update detail with confirm")
    public ResponseEntity<AppResponse<String>> updateDetailWithUsernameConfirm(@Valid @RequestBody CodeDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLang lang) {
        AppResponse<String> response = profileEntityService.updateProfileWithUsernameConfirm(dto, lang);
        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/filter")
    @Operation(summary = "filter", description = "Api for filter")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PageImpl<ProfileDto>> Filter(@RequestBody ProfileFilterDTO dto, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "12") int size) {
        PageImpl<ProfileDto> response = profileEntityService.filterForAdmin(dto, PageUtil.page(page), size);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/status/{id}")
    @Operation(summary = "change profile status", description = "Api for changes status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AppResponse<String>> changeStatus(@PathVariable("id") Integer id, @RequestBody ChangeProfileStatusDTO dto) {
        AppResponse<String> response = profileEntityService.changeStatusProfile(id, dto);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "change profile status", description = "Api for update visible and delete profile")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AppResponse<String>> deleteProfile(@PathVariable("id") Integer id) {
        AppResponse<String> response = profileEntityService.deleteProfile(id);
        return ResponseEntity.ok().body(response);
    }

}
