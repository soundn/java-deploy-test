package com.aspacelifetech.interviewservice.handlers;


import io.vertx.core.http.Cookie;
import io.vertx.core.http.CookieSameSite;

import java.time.Duration;

/**
 * @Author: Idris Ishaq
 * @Date: 21 Feb, 2024
 */


public class CookieHandler {


    public static Cookie createCookie(String key, String value) {
        Cookie cookie = Cookie.cookie(key, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setSameSite(CookieSameSite.STRICT);
        cookie.setMaxAge(Duration.ofMinutes(10).toSeconds());
        cookie.setDomain("https://interview.abaaapps.com:8185");
        return cookie;
    }

}
