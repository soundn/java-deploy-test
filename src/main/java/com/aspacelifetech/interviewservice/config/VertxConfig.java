
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

package com.aspacelifetech.interviewservice.config;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.HSTSHandler;
import io.vertx.ext.web.handler.XFrameHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author: Idris Ishaq
 * @Date: 24 Nov, 2024
 */


@Configuration
public class VertxConfig {


    @Bean
    public Vertx vertx() {
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setEventLoopPoolSize(150);
        return Vertx.builder()
                .with(vertxOptions)
                .build();
    }

    @Bean
    public Router router(Vertx vertx) {
        Router router = Router.router(vertx);
        router.route().handler(ctx -> {
            ctx.response()
                    // do not allow proxies to cache the data
                    .putHeader("Cache-Control", "no-store, no-cache")
                    // prevents Internet Explorer from MIME - sniffing a
                    // response away from the declared content-type
                    .putHeader("X-Content-Type-Options", "nosniff")
                    // Strict HTTPS (for about ~6Months)
                    .putHeader("Strict-Transport-Security", "max-age=" + 15768)
                    // IE8+ do not allow opening of attachments in the context of this resource
                    .putHeader("X-Download-Options", "noopen")
                    // enable XSS for IE
                    .putHeader("X-XSS-Protection", "1; mode=block")
                    // deny frames
                    .putHeader("X-FRAME-OPTIONS", "DENY");
            ctx.next();
        });

        router.post().handler(BodyHandler.create());
        router.route().handler(HSTSHandler.create());
        router.route().handler(XFrameHandler.create(XFrameHandler.DENY));
        router.route().handler(CorsHandler.create()
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowCredentials(true)
                .addRelativeOrigins(List.of("https://127.0.0.1:8185", "http://127.0.0.1:5500", "https://interview.abaaapps.com:8185"))
        );
        return router;
    }

}
