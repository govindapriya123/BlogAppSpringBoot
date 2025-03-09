package io.javabrains.Utilities;

import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import java.security.Key;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private  static final String SECRET_KEY = "aW5DbGtCZ1VmMXRrR3VjQm5yMTZodmptbkRWazNXVWtLZG1iNkxlWQ==";
    private static final long EXPIRATION_TIME=1000*60*60;
    private static final Key key=Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        private Key getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }


    public String generateToken(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 1))
                .signWith(getSigningKey(),SignatureAlgorithm.HS256).compact();
    }
    public String extractUsername(String token){
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getSubject();
    }
    public boolean validateToken(String token,String username){
        try{
         String extractedUsername=extractUsername(token);
         return (extractedUsername.equals(username)&&!isTokenExpired(token));
        }catch(Exception e){
         return false;
        }

    }
    private boolean isTokenExpired(String token){
        Date expiration=Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }
    public boolean isTokenValid(String token, String username) {
        return (extractUsername(token).equals(username) && !isTokenExpired(token));
    }

}
