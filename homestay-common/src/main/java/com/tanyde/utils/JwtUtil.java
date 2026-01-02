package com.tanyde.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
        //指定签名算法为Hs256
        SignatureAlgorithm signatureAlgorithm =SignatureAlgorithm.HS256;
        //生成JWT的时间
        long expMillis =System.currentTimeMillis()+ttlMillis;
        Date exp=new Date(expMillis);
        //设置jwt的body
        JwtBuilder builder= Jwts.builder()
                .setClaims(claims)
                .signWith(signatureAlgorithm,secretKey.getBytes(StandardCharsets.UTF_8))
                .setExpiration(exp);
        return builder.compact();
    }


    public static Claims parseJWT(String secretKey,String token){
        Claims claims=Jwts.parser()
                //设置秘钥签名
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                //设置徐亚解析的jwt
                .parseClaimsJws(token).getBody();
        return claims;
    }
}
