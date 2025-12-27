package com.frank.apisocialnetwork.dto;


public record UtilisateurDTO(
        int id,
        String nom,
        String prenom,
        ProfileDTO profile,
        Long totalLikes,
        Integer totalSuivis,
        Integer totalFollowers
) {
}
