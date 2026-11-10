package com.frank.apisocialnetwork.dto;


import java.time.Instant;

public record PublicationDTO(
        Integer id,
        String message,
        String urlPhoto,
        String urlVideo,
        UserSummaryDTO author,
        long commentCount,
        long likeCount,
        boolean isAuthor,
        boolean likedByConnectedUser,
        boolean followingAuthor,
        Instant createdAt
) {}

