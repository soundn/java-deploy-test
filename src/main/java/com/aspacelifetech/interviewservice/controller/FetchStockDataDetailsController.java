
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

package com.aspacelifetech.interviewservice.controller;

import com.aspacelifetech.interviewservice.config.ConfigData;
import com.aspacelifetech.interviewservice.model.StockData;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @Author: Idris Ishaq
 * @Date: 06 Dec, 2024
 */


@Controller
public class FetchStockDataDetailsController implements Handler<RoutingContext> {


    @Autowired
    public FetchStockDataDetailsController(Router router) {
        router.get("/fetch_stock_data/:stockName").handler(this);
    }

    @Override
    public void handle(RoutingContext ctx) {
        String stockName = ctx.request().getParam("stockName", "btc");

        int i = 1;
        JsonArray jsonArray = new JsonArray();
        for (String day : StockData.getInstance().getDays()) {
            JsonObject json = new JsonObject();
            json.put("day", day);
            json.put("price", StockData.getInstance().calculate(i));
            jsonArray.add(json);
            i += 1;
        }

        JsonObject json = new JsonObject();
        json.put("success", true);
        json.put("statusCode", 200);
        json.put("message", "Fetch Stock Data was Successful");
        json.put("dataSuccess", new JsonObject()
                .put("gatewayResponse", "Fetch Stock Data Request was Successful")
                .put("stockData", jsonArray));

        ctx.response()
                .setStatusCode(200)
                .putHeader(ConfigData.CONTENT_TYPE, ConfigData.JSON_CONTENT)
                .send(json.encode());
    }


}
