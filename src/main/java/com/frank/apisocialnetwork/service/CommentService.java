package com.frank.apisocialnetwork.service;

import com.frank.apisocialnetwork.dto.InputCommentDTO;
import com.frank.apisocialnetwork.dto.OutPutCommentDTO;
import com.frank.apisocialnetwork.dto.UserSummaryDTO;
import com.frank.apisocialnetwork.entity.Comment;
import com.frank.apisocialnetwork.entity.Publication;
import com.frank.apisocialnetwork.entity.Utilisateur;
import com.frank.apisocialnetwork.repository.CommentRepository;
import com.frank.apisocialnetwork.repository.PublicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@AllArgsConstructor
@Service
public class CommentService {

    private CommentRepository commentRepository;
    private PublicationRepository publicationRepository;

    public OutPutCommentDTO addComment(InputCommentDTO comment) {

        Comment newComment = new Comment();

        Optional<Publication> publication = publicationRepository.findById(comment.PublicationId());
        publication.ifPresent(newComment::setPublication);

        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newComment.setUtilisateur(utilisateur);
        newComment.setMessage(comment.message());

        Comment savedComment = commentRepository.save(newComment);

        UserSummaryDTO author = new UserSummaryDTO(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getProfile().getUrlPhotoProfile()
        );

        return new OutPutCommentDTO(
                savedComment.getId(),
                publication.get().getId(),
                savedComment.getMessage(),
                author,
                savedComment.getCreatedAt()
        );
    }


    public ResponseEntity<List<OutPutCommentDTO>> getAllCommentByPostId(Integer postId) {

        List<OutPutCommentDTO> commentsList = commentRepository.findByPublicationId(postId).stream()
                .map(comment -> {

                    UserSummaryDTO author = new UserSummaryDTO(
                            comment.getUtilisateur().getId(),
                            comment.getUtilisateur().getNom(),
                            comment.getUtilisateur().getPrenom(),
                            comment.getUtilisateur().getProfile().getUrlPhotoProfile()
                    );

                    return new OutPutCommentDTO(
                            comment.getId(),
                            comment.getPublication().getId(),
                            comment.getMessage(),
                            author,
                            comment.getCreatedAt()
                    );
                }).collect(Collectors.toList());

        return new ResponseEntity<>(commentsList, HttpStatus.OK);
    }





}
