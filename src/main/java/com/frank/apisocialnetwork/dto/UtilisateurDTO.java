package com.frank.apisocialnetwork.dto;


public record UtilisateurDTO(
        int id,
        String nom,
        String prenom,
        UserProfileDTO profile,
        Long totalLikes,
        Integer totalSuivis,
        Integer totalFollowers
) {
}
