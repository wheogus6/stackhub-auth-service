package auth.jwt;

import auth.config.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    private SecretKey getSecretKey() {

        return Keys.hmacShaKeyFor(
                jwtProperties.getSecretKey().getBytes()
        );
    }

    public String generateAccessToken(
            String id,
            String code,
            String sessionToken
    ) {

        return createToken(
                id,
                code,
                sessionToken,
                jwtProperties.getAccessExpiration()
        );
    }

    public String generateRefreshToken(
            String id,
            String code,
            String sessionToken
    ) {

        return createToken(
                id,
                code,
                sessionToken,
                jwtProperties.getRefreshExpiration()
        );
    }

    private String createToken(
            String id,
            String code,
            String sessionToken,
            long expiration
    ) {

        return Jwts.builder()
                .subject(id)
                .claim("code", code)
                .claim("sessionToken", sessionToken)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis() + expiration
                        )
                )
                .signWith(getSecretKey())
                .compact();
    }

    public Claims getClaims(String token) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {

        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getId(String token) {
        return getClaims(token).getSubject();
    }

    public String getCode(String token) {
        return getClaims(token).get("code", String.class);
    }

    public String getSessionToken(String token) {
        return getClaims(token)
                .get("sessionToken", String.class);
    }


}
