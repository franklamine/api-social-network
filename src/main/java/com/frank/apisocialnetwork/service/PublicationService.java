package com.frank.apisocialnetwork.service;

import com.frank.apisocialnetwork.dto.PublicationDTO;
import com.frank.apisocialnetwork.dto.UserSummaryDTO;
import com.frank.apisocialnetwork.entity.Publication;
import com.frank.apisocialnetwork.entity.Utilisateur;
import com.frank.apisocialnetwork.exception.ApiSocialNetworkException;
import com.frank.apisocialnetwork.repository.CommentRepository;
import com.frank.apisocialnetwork.repository.LikePostRepository;
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
    private final LikePostRepository likePostRepository;
    private final CommentRepository commentRepository;

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

        Utilisateur connectedUser = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<PublicationDTO> publicationDTOS = publicationRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(post -> {

                    UserSummaryDTO author = new UserSummaryDTO(
                            post.getUtilisateur().getId(),
                            post.getUtilisateur().getNom(),
                            post.getUtilisateur().getPrenom(),
                            post.getUtilisateur().getProfile().getUrlPhotoProfile()
                    );

                    return new PublicationDTO(
                            post.getId(),
                            post.getMessage(),
                            post.getUrlPhoto(),
                            post.getUrlVideo(),
                            author,
                            commentRepository.countByPublication(post),
                            likePostRepository.countByPublication(post),
                            post.getUtilisateur().getId().equals(connectedUser.getId()),
                            likePostRepository.existsByUtilisateurAndPublication(connectedUser, post),
                            post.getUtilisateur().getFollowers().contains(connectedUser),
                            post.getCreatedAt()
                    );
                }).collect(Collectors.toList());

        return new ResponseEntity<>(publicationDTOS, HttpStatus.OK);
    }


    public ResponseEntity<List<PublicationDTO>> getAllPublicationsByUserId(Integer userId) {

        Utilisateur connectedUser = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<PublicationDTO> publicationDTOS = publicationRepository.findByUtilisateurIdOrderByCreatedAtDesc(userId).stream()
                .map(post -> {

                    UserSummaryDTO author = new UserSummaryDTO(
                            post.getUtilisateur().getId(),
                            post.getUtilisateur().getNom(),
                            post.getUtilisateur().getPrenom(),
                            post.getUtilisateur().getProfile().getUrlPhotoProfile()
                    );

                    return new PublicationDTO(
                            post.getId(),
                            post.getMessage(),
                            post.getUrlPhoto(),
                            post.getUrlVideo(),
                            author,
                            commentRepository.countByPublication(post),
                            likePostRepository.countByPublication(post),
                            post.getUtilisateur().getId().equals(connectedUser.getId()),
                            likePostRepository.existsByUtilisateurAndPublication(connectedUser, post),
                            post.getUtilisateur().getFollowers().contains(connectedUser),
                            post.getCreatedAt()
                    );
                }).collect(Collectors.toList());

        return new ResponseEntity<>(publicationDTOS, HttpStatus.OK);
    }

}
