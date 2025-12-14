package com.frank.apisocialnetwork.service;

import com.frank.apisocialnetwork.entity.Utilisateur;
import com.frank.apisocialnetwork.exception.ApiSocialNetworkException;
import com.frank.apisocialnetwork.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



@AllArgsConstructor
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final CloudinaryService cloudinaryService;

    public ResponseEntity<String> addOrUpdatePhotoProfile(MultipartFile photoProfile) {

        try {
            Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(photoProfile != null && !photoProfile.isEmpty() && photoProfile.getContentType() != null && photoProfile.getContentType().startsWith("image/")) {
                utilisateur.getProfile().setUrlPhotoProfile(cloudinaryService.uploadFile(photoProfile));
            }

            profileRepository.save(utilisateur.getProfile());

        } catch (Exception e) {
            throw new ApiSocialNetworkException("Une erreur est survenue lors de la mise à jour de la photo de profil", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Photo de profil mise à jour avec succès !", HttpStatus.CREATED);
    }


    public ResponseEntity<String> addOrUpdatephotoCouverture(MultipartFile photoCouverture) {
        try {
            Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(photoCouverture != null && !photoCouverture.isEmpty() && photoCouverture.getContentType() != null && photoCouverture.getContentType().startsWith("image/")) {
                utilisateur.getProfile().setUrlPhotoCouverture(cloudinaryService.uploadFile(photoCouverture));
            }

            profileRepository.save(utilisateur.getProfile());
        } catch (Exception e) {
            throw new ApiSocialNetworkException("Une erreur est survenue lors de la mise à jour de la photo de couverture", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Photo de couverture mise à jour avec succès !", HttpStatus.CREATED);
    }

    public ResponseEntity<String> addOrUpdateBio(String bio) {

        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        utilisateur.getProfile().setBio(bio);

        return new ResponseEntity<>("Bio mie à jour avec succès !", HttpStatus.CREATED);
    }
}
