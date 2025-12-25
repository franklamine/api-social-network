package com.frank.apisocialnetwork.dto;


import java.time.Instant;
import java.util.List;

public record PublicationDTO(
        int id,
        int idAuteur,
        String message,
        String urlPhoto,
        String urlVideo,
        String auteurPublication,
        String photoAuteurPublication,
        List<CommentDTO> comments,
        int likes,
        Instant date
) {}

