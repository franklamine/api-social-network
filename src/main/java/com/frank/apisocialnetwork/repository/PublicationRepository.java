package com.frank.apisocialnetwork.repository;

import com.frank.apisocialnetwork.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, Integer> {

    List<Publication> findAllByOrderByCreatedAtDesc();

    Integer countByUtilisateurId(Integer utilisateurId);

    List<Publication> findByUtilisateurIdOrderByCreatedAtDesc(Integer utilisateurId);
}

