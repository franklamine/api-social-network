package com.frank.apisocialnetwork.repository;

import com.frank.apisocialnetwork.entity.LikePost;
import com.frank.apisocialnetwork.entity.Publication;
import com.frank.apisocialnetwork.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface LikePostRepository extends JpaRepository<LikePost, Long> {
    Optional<LikePost> findByUtilisateurAndPublication(Utilisateur connectedUser, Publication publication);

    Long countByPublication(Publication publication);

    @Query("SELECT COUNT(l) FROM LikePost l WHERE l.publication.utilisateur.id = :userId")
    Long getTotalLikesByUser(@Param("userId") Integer userId);

}
