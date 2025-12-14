package com.frank.apisocialnetwork.service;

import com.frank.apisocialnetwork.dto.CommentDTO;
import com.frank.apisocialnetwork.dto.PublicationDTO;
import com.frank.apisocialnetwork.entity.Profile;
import com.frank.apisocialnetwork.entity.Publication;
import com.frank.apisocialnetwork.entity.Utilisateur;
import com.frank.apisocialnetwork.exception.ApiSocialNetworkException;
import com.frank.apisocialnetwork.repository.PublicationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class PublicationService {
    private final PublicationRepository publicationRepository;
    private final CloudinaryService cloudinaryService;

    public ResponseEntity<String> creerPublication(String message, MultipartFile photo, MultipartFile video) {
        try {
            Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Publication publication = new Publication();
            publication.setUtilisateur(utilisateur);
            publication.setMessage(message);

            if (photo != null && !photo.isEmpty()) {
                if (!photo.getContentType().startsWith("image/")) {
                    throw new ApiSocialNetworkException("Fichier image non valide", HttpStatus.BAD_REQUEST);
                }
                publication.setUrlPhoto(cloudinaryService.uploadFile(photo));
            }
            if (video != null && !video.isEmpty()) {
                if (!video.getContentType().startsWith("video/")) {
                    throw new ApiSocialNetworkException("Fichier vidéo non valide", HttpStatus.BAD_REQUEST);
                }
                publication.setUrlVideo(cloudinaryService.uploadFile(video));
            }

            publicationRepository.save(publication);
        } catch (Exception e) {
            throw new ApiSocialNetworkException("une ereur est survenue pendant la publication", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("publication effectuée", HttpStatus.CREATED);
    }

    public ResponseEntity<List<PublicationDTO>> getAllPublications() {

        List<PublicationDTO> publicationDTOS = publicationRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(publication -> {

                    String auteurPublication = publication.getUtilisateur().getNom() + " " + publication.getUtilisateur().getPrenom();

                    Profile profile = publication.getUtilisateur().getProfile();
                    String photoAuteurPublicationUrl = profile.getUrlPhotoProfile();

                    List<CommentDTO> commentDTOs = publication.getComments().stream()
//                              .sorted((c1, c2) -> c2.getId().compareTo(c1.getId())) // tri du plus récent au plus ancien
                            .map(comment -> {
                                Utilisateur utilisateur = comment.getUtilisateur();
                                Profile profile1 = utilisateur.getProfile();
                                String photoAuteurComentUrl = profile1.getUrlPhotoProfile();
                                return new CommentDTO(
                                        publication.getId(),
                                        comment.getMessage(),
                                        comment.getLikes(),
                                        utilisateur.getNom() + " " + utilisateur.getPrenom(),
                                        photoAuteurComentUrl
                                );
                            }).collect(Collectors.toList());

                    return new PublicationDTO(
                            publication.getId(),
                            publication.getUtilisateur().getId(),
                            publication.getMessage(),
                            publication.getUrlPhoto(),
                            publication.getUrlVideo(),
                            auteurPublication,
                            photoAuteurPublicationUrl,
                            commentDTOs,
                            publication.getLikes(),
                            publication.getCreatedAt()
                    );
                }).collect(Collectors.toList());

        return new ResponseEntity<>(publicationDTOS, HttpStatus.OK);
    }

}
