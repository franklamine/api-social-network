package com.frank.apisocialnetwork.dto;

public record UserProfileDTO(
        Integer id,
        String nom,
        String prenom,
        String photoProfil,
        String photoCouverture,
        String bio,
        Integer publicationCount,
        Integer followerCount,
        Integer followingCount,
        Long  receivedLikeCount,
        boolean isProfileOwner,
        boolean isFollowedByConnectedUser
        ) {
}
