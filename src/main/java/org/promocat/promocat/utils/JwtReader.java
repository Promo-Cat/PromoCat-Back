package org.promocat.promocat.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

public class JwtReader {

    private final Claims jwtBody;

    public JwtReader(String jwt) {
        JwtParser jwtParser = Jwts.parserBuilder().build();
        //TODO: секретный ключ для шифрования JWT
        jwtBody = jwtParser.parseClaimsJwt(jwt).getBody();
    }

    public String getValue(String key) {
        return (String) jwtBody.get(key);
    }

    public <T> T getValue(String key, Class<T> tClass) {
        return jwtBody.get(key, tClass);
    }

}
