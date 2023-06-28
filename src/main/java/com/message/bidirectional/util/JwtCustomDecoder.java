package com.message.bidirectional.util;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtCustomDecoder implements JwtDecoder {
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            JWT parsedJWT = JWTParser.parse(token);
            return createJwt(token, parsedJWT);
        } catch (ParseException e) {
            log.info("");
            throw new RuntimeException(e);
        }
    }
    private Jwt createJwt(String token, JWT parsedJwt) {
        try {
            Map<String, Object> headers = new LinkedHashMap<>(parsedJwt.getHeader().toJSONObject());
            Map<String, Object> claims = new HashMap<>(parsedJwt.getJWTClaimsSet().getClaims());
            Instant iat = convertToInstant(claims.get("iat"));
            Instant exp = convertToInstant(claims.get("exp"));

            claims.remove("iat",claims.get("iat"));
            claims.remove("exp",claims.get("exp"));
            if(iat != null && exp != null){
                claims.put("iat",iat);
                claims.put("exp",exp);
            }
            Jwt.Builder jwtBuilder = Jwt.withTokenValue(token);
            jwtBuilder.headers(h -> h.putAll(headers));
            jwtBuilder.claims(c -> c.putAll(claims));
            Jwt jwt = jwtBuilder.build();
            return jwt;
        } catch (Exception ex) {
            log.error("dio");
        }
        return Jwt.withTokenValue(token).build();
    }

    private Instant convertToInstant(Object obj){
        if(obj != null && Date.class.isAssignableFrom(obj.getClass())){
            return Instant.ofEpochMilli(((Date)obj).getTime());
        }
        return null;
    }
}
