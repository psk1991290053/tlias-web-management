package com.itheima;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    //生成JWT令牌
    @Test
    public void testGenerateJwt(){
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("id",1);
        dataMap.put("username","admin");

        String jwt = Jwts.builder().signWith(SignatureAlgorithm.HS256,"aXRoZWltYQ==")//制定加密算法，秘钥
                .addClaims(dataMap) //添加自定义信息
                .setExpiration(new Date(System.currentTimeMillis() + 3600*1000)) //设置过期时间
                .compact();//生成令牌
        System.out.println(jwt);
    }

    //解析JWT令牌
    @Test
    public void testParseJwt() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJhZG1pbiIsImV4cCI6MTc4NDk5MDM0OH0.LR8MV1Q8xJzG1AZipsWo-IIfCD4vfLhlAYgBLyhVm_4";
        Claims claims = Jwts.parser().setSigningKey("aXRoZWltYQ==")//指定秘钥
                .parseClaimsJws(token)//解析令牌
                .getBody();//获取自定义信息
        System.out.println(claims);
    }

}
