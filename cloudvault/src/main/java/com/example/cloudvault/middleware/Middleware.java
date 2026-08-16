package com.example.cloudvault.middleware;

import com.example.cloudvault.dto.UserDt;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.Keys;
import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class Middleware extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private static final String JWT_SECRET =
            "secret-key";
    private static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    public Middleware(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private String getTkn(HttpServletRequest request) throws IOException {
        String secretKey = "secret-key" ;
        String authHeader = request.getHeader("X-USER-TOKEN");

        if (authHeader == null || authHeader.isBlank()) {
            throw new IOException("JWT token missing");
        }

        // If header is: Bearer eyJhbGciOi...
        String token = authHeader;

        if (authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token.isBlank()) {
            throw new IOException("JWT token missing");
        }

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // JWT expiration is automatically validated by JJWT
            // when the "exp" claim is present.

            String userId = claims.get("userId", String.class);

            if (userId == null || userId.isBlank()) {
                throw new IOException("userId missing from JWT");
            }

            return userId;

        } catch (ExpiredJwtException e) {
            throw new IOException("JWT token expired", e);

        } catch (JwtException e) {
            throw new IOException("Invalid JWT token", e);

        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid JWT token", e);
        }
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

try{
    String body = getTkn(request);
    // verify user info available
//    UserDt userDt = objectMapper.readValue(body, UserDt.class);
    request.setAttribute("userId", body);
    filterChain.doFilter(request, response);

}catch (Exception e){
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    response.setContentType("application/json");
    response.getWriter().write("""
        {
            "error": "Invalid request body"
        }
        """);
}
    }
}