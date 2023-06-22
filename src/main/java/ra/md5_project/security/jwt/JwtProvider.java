package ra.md5_project.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ra.md5_project.security.userPrincipal.UserPrincipal;


import java.util.Date;

@Component
public class JwtProvider {
    private final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    private static final String SECRET_KEY = "hungvv";
    private static final long TOKEN_EXPIRED = 86400000;

    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + TOKEN_EXPIRED))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("Failed Expired Token -> Message {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Failed Unsupported Token -> Message {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Failed Invalid Format Token -> Message {}", e.getMessage());
        } catch (SignatureException e) {
            logger.error("Failed Signature Token -> Message {}", e.getMessage());
        }catch (IllegalArgumentException e) {
            logger.error("Failed Claim Empty Token -> Message {}", e.getMessage());
        }
        return false;
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

}
