package com.frank.apisocialnetwork.dto;


import java.time.LocalDateTime;

public record CommentDTO(
        int id,
        int PublicationId,
        String message,
        int like,
        String auteurComment,
        String photoAuteurComment,
        LocalDateTime date
) {
}

