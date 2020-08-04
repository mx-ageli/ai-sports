package com.mx.ai.sports.common.utils;

import com.alibaba.fastjson.JSON;
import com.mx.ai.sports.system.dto.UserSimple;
import com.mx.ai.sports.common.entity.AiSportsConstant;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * <p>jwt token工具类</p>
 * <pre>
 *  jwt的claim里一般包含以下几种数据:
 *  1. iss -- token的发行者
 *  2. sub -- 该JWT所面向的用户
 *  3. aud -- 接收该JWT的一方
 *  4. exp -- token的失效时间
 *  5. nbf -- 在此时间段之前,不会被处理
 *  6. iat -- jwt发布时间
 *  7. jti -- jwt唯一标识,防止重复使用
 * </pre>
 *
 * @author Mengjiaxin
 * @date 2019-08-20 16:26
 */
public class JwtTokenUtil {

    /**
     * 获取用户名从token中
     */
    public static String getUsernameFromToken(String token) {
        return getClaimFromToken(token).getSubject();
    }

    /**
     * 获取jwt发布时间
     */
    public static Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token).getIssuedAt();
    }

    /**
     * 获取jwt失效时间
     */
    public static Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token).getExpiration();
    }

    /**
     * 获取jwt接收者
     */
    public static String getAudienceFromToken(String token) {
        return getClaimFromToken(token).getAudience();
    }

    /**
     * 获取私有的jwt claim
     */
    public static String getPrivateClaimFromToken(String token, String key) {
        return getClaimFromToken(token).get(key).toString();
    }

    /**
     * 获取jwt的payload部分
     */
    public static Claims getClaimFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(AiSportsConstant.SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 解析token是否正确,不正确会报异常<br>
     */
    public static void parseToken(String token) throws JwtException {
        Jwts.parser().setSigningKey(AiSportsConstant.SECRET).parseClaimsJws(token).getBody();
    }

    /**
     * <pre>
     *  验证token是否失效
     *  true:过期   false:没过期
     * </pre>
     */
    public static Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException expiredJwtException) {
            return true;
        }
    }

    /**
     * 生成token(通过用户名和签名时候用的随机数)
     */
    public static String generateToken(UserSimple userSimple) {
        Map<String, Object> claims = new HashMap<>(0);

        String subject = JSON.toJSONString(userSimple);
        return doGenerateToken(claims, subject);
    }

    /**
     * 生成token
     */
    private static String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + AiSportsConstant.EXPIRATION);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, AiSportsConstant.SECRET)
                .compact();
    }

    /**
     * 获取混淆MD5签名用的随机字符串
     */
    public static String getRandomKey() {
        return AiSportsUtil.getRandomString(6);
    }

    /**
     * 产生6位的随机数字
     *
     * @return
     */
    public static String getRandomCode() {

        int flag = new Random().nextInt(999999);
        if (flag < 100000) {
            flag += 100000;
        }

        return String.valueOf(flag);
    }

    /**
     * 随机生成密码
     *
     * @param length 密码的长度
     * @return 最终生成的密码
     */
    public static String generatePassword(int length) {
        // 最终生成的密码
        String password = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 随机生成0或1，用来确定是当前使用数字还是字母 (0则输出数字，1则输出字母)
            int charOrNum = random.nextInt(2);
            if (charOrNum == 1) {
                // 随机生成0或1，用来判断是大写字母还是小写字母 (0则输出小写字母，1则输出大写字母)
                int temp = random.nextInt(2) == 1 ? 65 : 97;
                password += (char) (random.nextInt(26) + temp);
            } else {
                // 生成随机数字
                password += random.nextInt(10);
            }
        }
        return password;
    }

}