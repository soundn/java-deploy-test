
/*
 * Copyright 2023 The Abaa Technology Solutions Limited
 *
 * The Abaaly Project licenses this file to you under the AbaaTech Solutions Limited License,
 * version 1.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *   https://www.abaatechsolutions.com/abaaly-licenses/LICENSE-1.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.aspacelifetech.interviewservice.filter;

import com.aspacelifetech.interviewservice.config.AbaaJwtSigningCertificateBuilder;
import com.aspacelifetech.interviewservice.config.ConfigData;
import com.aspacelifetech.interviewservice.handlers.AbaaJwtAuthenticationHandler;
import com.aspacelifetech.interviewservice.handlers.ErrorResponseUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.vertx.core.Handler;
import io.vertx.core.http.Cookie;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Idris Ishaq
 * @Date: 06 Dec, 2024
 */


@Component
public class AuthenticationFilter implements Handler<RoutingContext> {

    private static final String MSG = "Authentication Token Required!";
    private static final String G_MSG = "Invalid Authentication Token.!";


    private final AbaaJwtAuthenticationHandler jwtAuthenticationHandler;


    @Autowired
    public AuthenticationFilter(Router router, AbaaJwtAuthenticationHandler jwtAuthenticationHandler) {
        this.jwtAuthenticationHandler = jwtAuthenticationHandler;
        router.route().order(1).handler(this);
    }


    @Override
    public void handle(RoutingContext ctx) {

        try {
            HttpServerRequest httpRequest = ctx.request();
            String uri = httpRequest.uri();

            if (uri.equalsIgnoreCase("/") || uri.equalsIgnoreCase("/api-doc")
                    || uri.startsWith("/doc") || uri.startsWith("/auth_login") || uri.startsWith("/bit_coin.png")
                    || uri.startsWith("/eth_coin.png") || uri.startsWith("/light_coin.png") || uri.startsWith("/xrp_coin.png")) {
                ctx.next();
            } else {

                Cookie authCookie = httpRequest.getCookie("_auth_cookie");
                if (authCookie != null) {
                    JwtParserBuilder builder = Jwts.parser();
                    builder.decryptWith(AbaaJwtSigningCertificateBuilder.getPrivateKey());
                    builder.requireIssuer(ConfigData.ISSUER_NAME);
                    JwtParser jwtParser = builder.build();

                    Claims claims = jwtParser.parseEncryptedClaims(authCookie.getValue()).getPayload();
                    claims.getSubject();
                    ctx.next();
                } else {
                    build(ctx.response());
                }
            }
        } catch (Exception e) {
            ErrorResponseUtil.doBuild(ctx, e);
        }
    }

    private void build(HttpServerResponse response) {
        response.setStatusCode(401)
                .putHeader(ConfigData.CONTENT_TYPE, ConfigData.JSON_CONTENT)
                .end(ErrorResponseUtil.doBuild(401, AuthenticationFilter.MSG, AuthenticationFilter.G_MSG));
    }

}
