package com.message.bidirectional.service;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtDecoderService implements JwtDecoder {
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
            Map<String, Object> claims = parsedJwt.getJWTClaimsSet().getClaims();
            return Jwt.withTokenValue(token)
                    .headers(h -> h.putAll(headers))
                    .claims(c -> c.putAll(claims))
                    .build();
        } catch (Exception ex) {
            log.error("dio");
        }
        return Jwt.withTokenValue(token).build();
    }
}
