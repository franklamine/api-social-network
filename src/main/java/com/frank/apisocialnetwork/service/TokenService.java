package com.frank.apisocialnetwork.service;

import com.frank.apisocialnetwork.entity.Token;
import com.frank.apisocialnetwork.entity.Utilisateur;
import com.frank.apisocialnetwork.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
@Service
public class TokenService {
    private CustomUserDetailsService customUserDetailsService;
    private TokenRepository tokenRepository;


    private long calculExpiration(long duree){return duree * 60 * 1000; }

    public String generateToken(String username, boolean isAccessToken) {

        long expiration = isAccessToken ? calculExpiration(7L*24*60) : calculExpiration(30L*24*60);
        Utilisateur utilisateur = (Utilisateur) customUserDetailsService.loadUserByUsername(username);
        final Map<String, Object> claims = new HashMap<>(Map.of("nom", utilisateur.getNom(),"prenom" ,utilisateur.getPrenom(), "roles",utilisateur.getAuthorities()));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(utilisateur.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        String ENCRYPTION_KEY = "MaCleSecreteTresLongueQuiFaitAuMoins32Caracteres!";
        final byte[] decoder = Decoders.BASE64.decode(ENCRYPTION_KEY);

        return Keys.hmacShaKeyFor(decoder);
    }

    public String extractUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = getClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    private <T> T getClaim(String token, Function<Claims, T> function) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Map<String, String> refreshToken(Map<String, String> refreshToken) {
        String valeurRefresh = refreshToken.get("token");
        List<Token> tokens = tokenRepository.findByRefreshToken(valeurRefresh);
        if (tokens.isEmpty()) {
            throw new RuntimeException("Token non trouve");
        }
        if (isTokenExpired(valeurRefresh)) {
            throw new RuntimeException("le token a expiré");
        }
        String accessToken = generateToken(tokens.get(0).getUtilisateur().getUsername(), true);
        Token tokenObj = new Token();
        tokenObj.setAccessToken(accessToken);
        tokenObj.setRefreshToken(valeurRefresh);
        tokenObj.setUtilisateur(tokens.get(0).getUtilisateur());
        tokenRepository.deleteAll();
        tokenRepository.save(tokenObj);
        return new HashMap<>(Map.of("accessToken",accessToken)) ;
    }
}
