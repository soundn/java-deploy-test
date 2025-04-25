
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

package com.aspacelifetech.interviewservice.controller.assets;

import com.aspacelifetech.interviewservice.config.ConfigData;
import io.vertx.core.Handler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @Author: Idris Ishaq
 * @Date: 06 Dec, 2024
 */


@Controller
public class XrpCoinAssetController implements Handler<RoutingContext> {


    @Autowired
    public XrpCoinAssetController(Router router) {
        router.get("/xrp_coin.png").handler(this);
    }

    @Override
    public void handle(RoutingContext ctx) {
        ctx.response().setStatusCode(200)
                .putHeader(ConfigData.CONTENT_TYPE, ConfigData.PNG_CONTENT)
                .sendFile("coinimages/xrpcoin.png");
    }

}
