package com.frank.apisocialnetwork.dto;


import java.time.Instant;

public record CommentDTO(
        int id,
        int PublicationId,
        String message,
        String auteurComment,
        String photoAuteurComment,
        Instant date
) {
}

