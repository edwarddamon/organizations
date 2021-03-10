package com.lhamster.util;

public class Audience {
    // 代表这个JWT的接收对象,存入audience
    public static String clientId = "1";
    // 密钥,经过Base64加密,可自行替换(important)
    public static String base64Secret = "edward.ZhiHuiXiaoYuan";
    // JWT的签发主体，存入issuer
    public static String name = "Damon_Edward";
    // 过期时间（配置成默认三十天失效），时间戳(单位是毫秒)
    public static Long expiresSecond = 30 * 24 * 60 * 60 * 1000L;
}