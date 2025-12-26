package com.frank.apisocialnetwork.controller;

import com.frank.apisocialnetwork.entity.Utilisateur;
import com.frank.apisocialnetwork.service.LikePostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "likeposts")
public class LikePostController {
    private LikePostService likePostService;

    @PostMapping(path = "{publicationID}")
    public ResponseEntity<Long> likedOrDislikedPublication(@PathVariable Integer publicationID, @AuthenticationPrincipal Utilisateur connectedUser) {
        Long countLike = likePostService.likedOrDislikedPublication(publicationID, connectedUser);
        return new ResponseEntity<>(countLike, HttpStatus.OK);
    }


    @GetMapping(path = "{userId}")
    public ResponseEntity<Long> getTotalLikesByUser(@PathVariable Integer userId) {
        Long totalLikes = likePostService.getTotalLikesByUser(userId);

        return new ResponseEntity<>(totalLikes, HttpStatus.OK);
    }

}
