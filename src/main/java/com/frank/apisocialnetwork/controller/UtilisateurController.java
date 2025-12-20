package com.frank.apisocialnetwork.controller;

import com.frank.apisocialnetwork.dto.UserConnectedDTO;
import com.frank.apisocialnetwork.dto.UtilisateurDTO;
import com.frank.apisocialnetwork.service.TokenService;
import com.frank.apisocialnetwork.dto.AuthentificationDTO;
import com.frank.apisocialnetwork.entity.Utilisateur;
import com.frank.apisocialnetwork.service.UtilisateurService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@Slf4j
//@CrossOrigin(origins = "https://lfsol.cloud")
@AllArgsConstructor
@RestController
@RequestMapping(path = "utilisateurs")
public class UtilisateurController {

    private UtilisateurService utilisateurService;
    private TokenService tokenService;


    @PostMapping(path = "inscription")
    public ResponseEntity<String> incription(@RequestBody Utilisateur utilisateur) {
//        log.info("Inscription Utilisateur");
        return utilisateurService.inscription(utilisateur);
    }

    @PostMapping(path = "activation")
    public ResponseEntity<String> activation(@RequestBody Map<String, String> codeActivation) {
        return utilisateurService.activation(codeActivation);
    }

    @PostMapping(path = "nouveau-code")
    public ResponseEntity<String> nouveauCodeActivation(@RequestBody Map<String, String> email) {
        return utilisateurService.nouveauCodeActivation(email);
    }

    @PostMapping(path = "connexion")
    public ResponseEntity<Map<String, String>>  connexion(@RequestBody AuthentificationDTO authentificationDTO, HttpServletResponse response) {
        return utilisateurService.connexion(authentificationDTO, response);
    }

    @PostMapping("refresh-token")
    public Map<String, String> refreshToken(@RequestBody Map<String, String> refreshToken) {
        return tokenService.refreshToken(refreshToken);
    }

    @PostMapping("deconnexion")
    public ResponseEntity<String> deconnexion(@RequestBody Map<String, String> refreshToken) {
        return utilisateurService.deconnexion(refreshToken);
    }

    @PostMapping("mot-de-passe-oublier")
    public ResponseEntity<String> modifierMotDePasse(@RequestBody Map<String, String> username) {
        return utilisateurService.modifierMotDePasse(username);
    }

    @PostMapping("nouveau-mot-de-passe")
    public ResponseEntity<String> nouveauMotDePasse(@RequestBody Map<String, String> parametres) {
        return utilisateurService.nouveauMotDePasse(parametres);
    }

    @GetMapping(path = "connected")
    public ResponseEntity<UserConnectedDTO> getConnectedUser() {
        return utilisateurService.getConnectedUser();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<UtilisateurDTO> getUserById(@PathVariable Integer id) {
        return utilisateurService.getUserById(id);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Integer id) {
        return utilisateurService.deleteUserById(id);
    }
}
