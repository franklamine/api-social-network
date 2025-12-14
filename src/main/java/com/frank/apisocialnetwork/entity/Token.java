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
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column( columnDefinition = "CLOB")
    private String refreshToken;

    @Lob
    @Column( columnDefinition = "CLOB")
    private String accessToken;

    @ManyToOne
    private Utilisateur utilisateur;

}
