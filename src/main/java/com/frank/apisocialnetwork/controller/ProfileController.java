package com.frank.apisocialnetwork.controller;

import com.frank.apisocialnetwork.entity.Profile;
import com.frank.apisocialnetwork.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping(path = "profile")
public class ProfileController {

    private ProfileService profileService;

    @PostMapping(path = "photo-profile")
    public ResponseEntity<String> addOrUpdatePhotoProfile(@RequestParam(value = "photoProfile") MultipartFile photoProfile) {
        return profileService.addOrUpdatePhotoProfile(photoProfile);
    }

    @PostMapping(path = "photo-couverture")
    public ResponseEntity<String> addOrUpdatePhotoCouverture(@RequestParam(value = "photoCouverture") MultipartFile photoCouverture) {
        return profileService.addOrUpdatephotoCouverture(photoCouverture);
    }

    @PostMapping(path = "bio")
    public ResponseEntity<String> addOrUpdateBio(@RequestBody String bio) {
        return profileService.addOrUpdateBio(bio);
    }

}
