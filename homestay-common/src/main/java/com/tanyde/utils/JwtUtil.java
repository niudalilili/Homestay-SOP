package com.tanyde.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class JwtUtil {
    /**
     * 生成jwt
     * 使用Hs256算法，私钥固定
     *
     * @param secretKey jet秘钥
     * @param ttlMillis jwt过期时间（毫秒）
     * @param claims    设置的信息
     * @return
     * @author TanyDe
     * @create 2026/1/2
     **/
    public static String createJWT(String secretKey,
                                   long ttlMillis,
                                   Map<String, Object>claims){
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        //指定签名算法为Hs256
        SignatureAlgorithm signatureAlgorithm =SignatureAlgorithm.HS256;
        //生成JWT的时间
        long expMillis =System.currentTimeMillis()+ttlMillis;
        Date exp=new Date(expMillis);
        //设置jwt的body
        JwtBuilder builder= Jwts.builder()
                .claims(claims)
                .signWith(key)
                .expiration(exp);
        return builder.compact();
    }


    public static Claims parseJWT(String secretKey,String token){
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        Claims claims=Jwts.parser()
                //设置秘钥签名
                .verifyWith(key)
                .build()
                .parseSignedClaims(token) // 0.11+ 使用 .parseSignedClaims(token) 替代 .parseClaimsJws(token)
                .getPayload();
        return claims;
    }
}
