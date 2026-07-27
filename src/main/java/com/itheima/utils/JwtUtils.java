package com.itheima.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/**
 * JWT令牌操作工具类
 * 提供JWT令牌的生成和解析功能
 */
public class JwtUtils {

    // 秘钥（与测试类中保持一致）
    private static final String SECRET_KEY = "aXRoZWltYQ==";

    // 令牌过期时间：12小时（单位：毫秒）
    private static final long EXPIRATION_TIME = 12 * 3600 * 1000;

    /**
     * 生成JWT令牌
     *
     * @param claims 自定义信息（键值对形式）
     * @return 生成的JWT令牌字符串
     */
    public static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 指定加密算法和秘钥
                .addClaims(claims) // 添加自定义信息
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 设置过期时间（12小时）
                .compact(); // 生成令牌
    }

    /**
     * 解析JWT令牌
     *
     * @param token JWT令牌字符串
     * @return 令牌中包含的自定义信息（Claims对象）
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY) // 指定秘钥
                .parseClaimsJws(token) // 解析令牌
                .getBody(); // 获取自定义信息
    }
}