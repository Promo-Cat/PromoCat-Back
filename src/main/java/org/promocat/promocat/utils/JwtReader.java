package org.promocat.promocat.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.promocat.promocat.util_entities.TokenService;

public class JwtReader {

    private final Claims jwtBody;

    public JwtReader(String jwt) {
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(TokenService.JWT_KEY.getBytes())).build();
        jwtBody = jwtParser.parseClaimsJws(jwt).getBody();
    }

    public String getValue(String key) {
        return (String) jwtBody.get(key);
    }

    public <T> T getValue(String key, Class<T> tClass) {
        return jwtBody.get(key, tClass);
    }

}
