package com.frank.apisocialnetwork.repository;

import com.frank.apisocialnetwork.entity.LikePost;
import com.frank.apisocialnetwork.entity.Publication;
import com.frank.apisocialnetwork.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface LikePostRepository extends JpaRepository<LikePost, Long> {
    Optional<LikePost> findByUtilisateurAndPublication(Utilisateur connectedUser, Publication publication);

    Long countByPublication(Publication publication);
}
