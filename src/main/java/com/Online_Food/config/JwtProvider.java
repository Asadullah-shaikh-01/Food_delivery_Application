package com.Online_Food.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service // Marks this class as a Spring service (a component managed by Spring)
public class JwtProvider {

    // 🔒 Secret key used to sign and verify JWT tokens
    // The key is created from a constant secret string in JwtConstant class
    private final SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());


    public String generateToken(Authentication auth) {

        // Get all roles/authorities of the authenticated user
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        // Convert authorities to a comma-separated string (e.g. "ROLE_USER,ROLE_ADMIN")
        String roles = populateAuthorities(authorities);
        // 🧩 Build the JWT token
        String jwt = Jwts.builder()
                .setIssuedAt(new Date()) // Token creation time
                .setExpiration(new Date(new Date().getTime() + 86400000)) // Token expiration (1 day = 86400000 ms)
                .claim("email", auth.getName()) // Add custom claim: user’s email or username
                .claim("authorities", roles) // Add custom claim: user roles
                .signWith(key) // Sign token using the secret key
                .compact(); // Build and serialize JWT into a compact string format

        // Return the generated JWT
        return jwt;
    }


    public  String getEmailFromJwtToken (String jwt){
        jwt=jwt.substring(7);
        // ✅ Updated parser for JJWT 0.13.0
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        // ✅ Extract custom fields from token
        String email = String.valueOf(claims.get("email"));

        return  email;
    }


    /**
     * 🔹 Converts authorities (roles) collection into a comma-separated string.
     * Example: [ROLE_USER, ROLE_ADMIN] → "ROLE_USER,ROLE_ADMIN"
     */
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>(); // Use Set to avoid duplicate roles

        // Loop through each granted authority and add to Set
        for (GrantedAuthority authority : authorities) {
            auths.add(authority.getAuthority());
        }

        // Join all roles into a single string separated by commas
        return String.join(",", auths);
    }

}
