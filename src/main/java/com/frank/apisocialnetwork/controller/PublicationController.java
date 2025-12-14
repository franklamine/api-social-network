package com.frank.apisocialnetwork.controller;

import com.frank.apisocialnetwork.dto.PublicationDTO;
import com.frank.apisocialnetwork.service.PublicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping(path = "publications")
public class PublicationController {

    private final PublicationService publicationService;

    @PostMapping(path = "publier")
    public ResponseEntity<String> creerAvis(@RequestParam("message") String message,
                                            @RequestParam(value = "photo", required = false) MultipartFile photo,
                                            @RequestParam(value = "video", required = false) MultipartFile video) {
        return publicationService.creerPublication(message, photo, video);
    }

    @GetMapping
    public ResponseEntity<List<PublicationDTO>> getAllPublications() {
        return publicationService.getAllPublications();
    }
}
