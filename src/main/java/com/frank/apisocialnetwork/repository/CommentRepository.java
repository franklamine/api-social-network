package com.frank.apisocialnetwork.repository;

import com.frank.apisocialnetwork.entity.Comment;
import com.frank.apisocialnetwork.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    long countByPublication(Publication publication);

    List<Comment> findByPublicationId(int publicationId);
}
