package com.frank.apisocialnetwork.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "publications")
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String message;
    private String urlPhoto;
    private String urlVideo;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @ManyToOne
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL)
    @OrderBy("createdAt ASC")
    private List<Comment> comments;

    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL)
    private List<LikePost> likePosts;


}
