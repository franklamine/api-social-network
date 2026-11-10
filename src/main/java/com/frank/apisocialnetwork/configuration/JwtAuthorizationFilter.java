package com.frank.apisocialnetwork.configuration;

import com.frank.apisocialnetwork.service.CustomUserDetailsService;
import com.frank.apisocialnetwork.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
@AllArgsConstructor
@Service
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private CustomUserDetailsService customUserDetailsService;
    private TokenService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String username = null;
        boolean isTokenExpired = true;
        final String authorizationToken = request.getHeader("Authorization");
        log.info("URI: {} | METHOD: {}", request.getRequestURI(), request.getMethod());

        if (authorizationToken != null && authorizationToken.startsWith("Bearer ")) {
            token = authorizationToken.substring(7);
            isTokenExpired = jwtService.isTokenExpired(token);
            username = jwtService.extractUsername(token);
        }

        if(!isTokenExpired && username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
         UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);

    }
}
