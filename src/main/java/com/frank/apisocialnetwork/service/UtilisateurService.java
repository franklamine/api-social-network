package com.frank.apisocialnetwork.service;

import com.frank.apisocialnetwork.dto.AuthentificationDTO;
import com.frank.apisocialnetwork.dto.ProfileDTO;
import com.frank.apisocialnetwork.dto.UserConnectedDTO;
import com.frank.apisocialnetwork.dto.UtilisateurDTO;
import com.frank.apisocialnetwork.entity.*;
import com.frank.apisocialnetwork.enumerateur.TypeRole;
import com.frank.apisocialnetwork.exception.ApiSocialNetworkException;
import com.frank.apisocialnetwork.repository.TokenRepository;
import com.frank.apisocialnetwork.repository.UtilisteurRepository;
import com.frank.apisocialnetwork.repository.ValidationRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Slf4j
@Transactional
@AllArgsConstructor
@Service
public class UtilisateurService {

    private UtilisteurRepository utilisateurRepository;
    private PasswordEncoder passwordEncoder;
    private ValidationService validationService;
    private ValidationRepository validationRepository;
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;
    private TokenRepository tokenRepository;
    private CustomUserDetailsService customUserDetailsService;


    public ResponseEntity<String> inscription(Utilisateur utilisateur) {
        log.info("Utilisateur inscription");
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findByEmail(utilisateur.getEmail());
        if (utilisateurOptional.isPresent()) {
            throw new ApiSocialNetworkException("Cet utilisateur existe déjà", HttpStatus.CONFLICT);
        }
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateur.setMotDePasseConfirmation(passwordEncoder.encode(utilisateur.getMotDePasseConfirmation()));
        utilisateur.setRole(new Role());
        utilisateur.getRole().setTypeRole(TypeRole.UTILISATEUR);
        utilisateur.setProfile(new Profile());
        utilisateur.getProfile().setUtilisateur(utilisateur);
        utilisateur = utilisateurRepository.save(utilisateur);

        validationService.enregistrerValidationEtNotifier(utilisateur);
        return new ResponseEntity<>("Inscription reussie. Utiliser le code de vérification recu par mail pour activer votre compte", HttpStatus.CREATED);
    }

    public ResponseEntity<String> activation(Map<String, String> codeActivation) {
        Validation validation = validationService.getValidationByCode(codeActivation.get("code"));
        if (Instant.now().isAfter(validation.getExpiration())) {
            throw new ApiSocialNetworkException("Le code d'activation a expiré. Veuillez en demander un nouveau.", HttpStatus.GONE);
        }
        Optional<Utilisateur> utilisateurAActiver = utilisateurRepository.findById(validation.getUtilisateur().getId());
        if (utilisateurAActiver.isEmpty()) {
            throw new ApiSocialNetworkException("Utilisateur n'existe pas", HttpStatus.NOT_FOUND); //pas util ici pour l'utilisateur
        }
        utilisateurAActiver.get().setActif(true);
        utilisateurRepository.save(utilisateurAActiver.get());
        validationRepository.delete(validation);
        return new ResponseEntity<>("cher " + utilisateurAActiver.get().getPrenom() + " votre compte a été activé .", HttpStatus.OK);
    }

    public ResponseEntity<String> nouveauCodeActivation(Map<String, String> email) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findByEmail(email.get("email"));
        if (utilisateur.isEmpty()) {
            throw new ApiSocialNetworkException("Utilisateur n'existe pas", HttpStatus.NOT_FOUND);
        }
        validationService.enregistrerValidationEtNotifier(utilisateur.get());

        return new ResponseEntity<>("Votre nouveau code d'activation a été envoyé a cet email:" +email, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, String>> connexion(AuthentificationDTO authentificationDTO, HttpServletResponse response) {
        Map<String, String> tokens = new HashMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authentificationDTO.email(), authentificationDTO.motDePasse()));
            if (authentication.isAuthenticated()) {
                String accessToken = tokenService.generateToken(authentificationDTO.email(), true);
                String refreshToken = tokenService.generateToken(authentificationDTO.email(), false);
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken);
                tokens.put("message", "Vous etes connecté !");
                Token tokenObj = new Token();
                tokenObj.setAccessToken(accessToken);
                tokenObj.setRefreshToken(refreshToken);
                tokenObj.setUtilisateur((Utilisateur) customUserDetailsService.loadUserByUsername(authentificationDTO.email()));
                tokenRepository.save(tokenObj);
            }
            return new ResponseEntity<>(tokens, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            throw new ApiSocialNetworkException("Email ou mot de passe invalide", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            throw new ApiSocialNetworkException("Erreur lors de l'authentification", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deconnexion(Map<String, String> refreshToken) {
        List<Token> tokens = tokenRepository.findByRefreshToken(refreshToken.get("token"));
        if (!tokens.isEmpty()) {
            for (Token s : tokens) {
                tokenRepository.delete(s);
            }
//            throw new ApiSocialNetworkException("Token non trouve",HttpStatus.MULTI_STATUS);
        }
        return new ResponseEntity<>("Déconnexion réussie", HttpStatus.OK);
    }

    public ResponseEntity<String> modifierMotDePasse(Map<String, String> username) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findByEmail(username.get("email"));
        if (utilisateur.isEmpty()) {
            throw new ApiSocialNetworkException("Utilisateur n'existe pas", HttpStatus.NOT_FOUND);
        }
        validationService.enregistrerValidationEtNotifier(utilisateur.get());
        return new ResponseEntity<>("un code de verification vous a été envoyé par email", HttpStatus.OK);
    }

    public ResponseEntity<String> nouveauMotDePasse(Map<String, String> parametres) {
        Validation validation = validationService.getValidationByCode(parametres.get("code"));
        if (Instant.now().isAfter(validation.getExpiration())) {
            throw new ApiSocialNetworkException("Le code d'activation a expiré. Veuillez en demander un nouveau.", HttpStatus.GONE);
        }
        Optional<Utilisateur> utilisateurAModifierMotDePasse = utilisateurRepository.findById(validation.getUtilisateur().getId());
        if (utilisateurAModifierMotDePasse.isEmpty()) {
            throw new ApiSocialNetworkException("Utilisateur n'existe pas", HttpStatus.NOT_FOUND); //pas util ici pour l'utilisateur
        }
        utilisateurAModifierMotDePasse.get().setMotDePasse(passwordEncoder.encode(parametres.get("nouveauMotDePasse")));
        utilisateurAModifierMotDePasse.get().setMotDePasseConfirmation(passwordEncoder.encode(parametres.get("confirmationMotDePasse")));
        utilisateurRepository.save(utilisateurAModifierMotDePasse.get());
        validationRepository.delete(validation);
        return new ResponseEntity<>("cher " + utilisateurAModifierMotDePasse.get().getPrenom() + " votre mot de pass a été réinitialisé.", HttpStatus.OK);
    }


    public ResponseEntity<UserConnectedDTO> getConnectedUser() {

        Utilisateur userConnected = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserConnectedDTO userConnectedDTO = new UserConnectedDTO(
                userConnected.getId(),
                userConnected.getNom(),
                userConnected.getPrenom(),
                userConnected.getProfile().getUrlPhotoProfile(),
                userConnected.getProfile().getUrlPhotoCouverture()
        );

        return new ResponseEntity<>(userConnectedDTO, HttpStatus.OK);

    }


    public ResponseEntity<UtilisateurDTO> getUserById(Integer id) {
        log.info(id.toString());
        Utilisateur utilisateur = utilisateurRepository
                .findById(id)
                .orElseThrow(() -> new ApiSocialNetworkException("Utilisateur non trouvé", HttpStatus.NOT_FOUND));

        Profile profile = utilisateur.getProfile();
        ProfileDTO profileDTO = new ProfileDTO(profile.getBio(), profile.getUrlPhotoProfile(), profile.getUrlPhotoCouverture());

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                profileDTO
        );

        return new ResponseEntity<>(utilisateurDTO, HttpStatus.OK);
    }


    public ResponseEntity<String> deleteUserById(Integer id) {
        utilisateurRepository.deleteById(id);
        return new ResponseEntity<>("utilisateur supprimer", HttpStatus.OK);
    }


}

