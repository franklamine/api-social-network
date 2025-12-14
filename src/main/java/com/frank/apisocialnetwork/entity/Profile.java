package com.frank.apisocialnetwork.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String bio;

    private String urlPhotoProfile;

    private String urlPhotoCouverture;

    @OneToOne
    @JoinColumn( nullable = false, unique = true)
    Utilisateur utilisateur;

}
