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

package com.aspacelifetech.interviewservice.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.ClientAuth;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.tracing.TracingPolicy;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Idris Ishaq
 * @Date: 24 Nov, 2024
 */


@Slf4j
@Component
public class WebVerticle extends AbstractVerticle {

    private final Router router;

    @Autowired
    public WebVerticle(Router router) {
        this.router = router;
    }


    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        startWebServer(startPromise);
    }

    private void startWebServer(Promise<Void> startPromise) {
        HttpServerOptions serverOptions = new HttpServerOptions()
                .setHost("0.0.0.0")
                //.setHost("45.90.122.16")
                .setPort(8185)
                .setAlpnVersions(List.of(HttpVersion.HTTP_1_1, HttpVersion.HTTP_2))
                .setUseAlpn(true)
                .setLogActivity(true)
                .setClientAuth(ClientAuth.REQUEST)
                .setTracingPolicy(TracingPolicy.ALWAYS);

        PemKeyCertOptions pemKeyCertOptions = new PemKeyCertOptions();
       // pemKeyCertOptions.setCertPath("/etc/letsencrypt/live/interview.abaaapps.com/fullchain.pem");
       // pemKeyCertOptions.setKeyPath("/etc/letsencrypt/live/interview.abaaapps.com/privkey.pem");

        pemKeyCertOptions.setCertPath("/Users/workspaces/abaa-social-feeds-backend-v2/abaa-social-feed-asset-server/src/main/resources/cert/abaa_server_certificate.crt");
        pemKeyCertOptions.setKeyPath("/Users/workspaces/abaa-social-feeds-backend-v2/abaa-social-feed-asset-server/src/main/resources/cert/abaa_server_private_certificate.key");

        //
        // serverOptions.setSsl(true);
        // serverOptions.setPemKeyCertOptions(pemKeyCertOptions);

        vertx.createHttpServer(serverOptions)
                .requestHandler(router)
                .listen().onComplete(ar -> {
                    if (ar.succeeded()) {
                        startPromise.complete();
                        log.info("Server is Running on Port: {}", ar.result().actualPort());
                    } else {
                        startPromise.fail(ar.cause());
                        log.error("WebServerVerticle", ar.cause());
                    }
                });
    }

}
