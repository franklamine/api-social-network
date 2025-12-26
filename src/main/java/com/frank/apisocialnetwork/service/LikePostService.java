package com.frank.apisocialnetwork.service;

import com.frank.apisocialnetwork.entity.LikePost;
import com.frank.apisocialnetwork.entity.Publication;
import com.frank.apisocialnetwork.entity.Utilisateur;
import com.frank.apisocialnetwork.exception.ApiSocialNetworkException;
import com.frank.apisocialnetwork.repository.LikePostRepository;
import com.frank.apisocialnetwork.repository.PublicationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class LikePostService {
    final private PublicationRepository publicationRepository;
    final private LikePostRepository likePostRepository;

    public Long likedOrDislikedPublication(Integer publicationID, Utilisateur connectedUser) {

        Publication publication = publicationRepository.findById(publicationID)
                .orElseThrow(() -> new ApiSocialNetworkException("Publication non trouvé", HttpStatus.NOT_FOUND));

        Optional<LikePost> existLikePost = likePostRepository.findByUtilisateurAndPublication(connectedUser, publication);

        if (existLikePost.isPresent()) {
            likePostRepository.delete(existLikePost.get());
        } else {
            LikePost likePost = new LikePost();
            likePost.setUtilisateur(connectedUser);
            likePost.setPublication(publication);
            likePostRepository.save(likePost);
        }
        return likePostRepository.countByPublication(publication);
    }


}
