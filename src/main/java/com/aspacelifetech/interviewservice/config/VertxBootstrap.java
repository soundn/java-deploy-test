
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

import com.aspacelifetech.interviewservice.verticle.WebVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Idris Ishaq
 * @Date: 24 Nov, 2024
 */


@Slf4j
@Component
public class VertxBootstrap implements CommandLineRunner {

    private final Vertx vertx;
    private final WebVerticle webVerticle;


    @Autowired
    public VertxBootstrap(Vertx vertx, WebVerticle webVerticle) {
        this.vertx = vertx;
        this.webVerticle = webVerticle;
    }


    @Override
    public void run(String... args) throws Exception {
        DeploymentOptions deploymentOptions = new DeploymentOptions();

        Future<String> webServer = Future.future(promise -> vertx.deployVerticle(webVerticle, deploymentOptions, promise));

        Future.all(List.of(webServer))
                .onComplete(event -> {
                    if (event.succeeded()) {
                        log.info("Deploy Verticle Successful!");
                    } else {
                        log.error("VertxBootstrap", event.cause());
                    }
                });
    }

}
