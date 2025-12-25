package com.frank.apisocialnetwork.service;

import com.frank.apisocialnetwork.dto.CommentDTO;
import com.frank.apisocialnetwork.entity.Comment;
import com.frank.apisocialnetwork.entity.Publication;
import com.frank.apisocialnetwork.entity.Utilisateur;
import com.frank.apisocialnetwork.repository.CommentRepository;
import com.frank.apisocialnetwork.repository.PublicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@AllArgsConstructor
@Service
public class CommentService {

    private CommentRepository commentRepository;
    private PublicationRepository publicationRepository;

    public CommentDTO addComment(CommentDTO comment) {

        Comment newComment = new Comment();

        Optional<Publication> publication = publicationRepository.findById(comment.PublicationId());
        publication.ifPresent(newComment::setPublication);

        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newComment.setUtilisateur(utilisateur);
        newComment.setMessage(comment.message());

        Comment savedComment = commentRepository.save(newComment);

        return new CommentDTO(
                savedComment.getId(),
                publication.get().getId(),
                savedComment.getMessage(),
                savedComment.getLikes(),
                utilisateur.getNom() + " " + utilisateur.getPrenom(),
                utilisateur.getProfile().getUrlPhotoProfile(),
                savedComment.getCreatedAt()
        );
    }
}
