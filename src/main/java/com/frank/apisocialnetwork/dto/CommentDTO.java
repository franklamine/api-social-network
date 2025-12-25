package com.frank.apisocialnetwork.dto;


import java.time.Instant;

public record CommentDTO(
        int id,
        int PublicationId,
        String message,
        int like,
        String auteurComment,
        String photoAuteurComment,
        Instant date
) {
}

