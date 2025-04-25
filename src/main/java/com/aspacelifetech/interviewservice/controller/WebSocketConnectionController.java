
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

import com.aspacelifetech.interviewservice.handlers.ErrorResponseUtil;
import com.aspacelifetech.interviewservice.model.StockData;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @Author: Idris Ishaq
 * @Date: 05 Dec, 2024
 */


@Controller
public class WebSocketConnectionController implements Handler<RoutingContext> {


    private final Vertx vertx;

    @Autowired
    public WebSocketConnectionController(Router router, Vertx vertx) {
        this.vertx = vertx;
        router.get("/socket_connect").handler(this);
    }

    @Override
    public void handle(RoutingContext ctx) {
        ctx.request().toWebSocket(asyncResult -> {
            if (asyncResult.succeeded()) {
                proceedSocketHandler(asyncResult.result(), ctx);
            } else {
                ErrorResponseUtil.doBuild(ctx, "Error", "");
            }
        });
    }

    private void proceedSocketHandler(ServerWebSocket webSocket, RoutingContext ctx) {
        vertx.setPeriodic(2000, as -> {

            int i = 1;
            StringBuilder sb = new StringBuilder();
            sb.append("<stockDataList>");
            for (StockData.Stock stock : StockData.getInstance().getStockData()) {
                sb.append("<stockData>");
                sb.append("<stockId>").append(stock.stockId()).append("</stockId>");
                sb.append("<stockName>").append(stock.stockName()).append("</stockName>");
                sb.append("<stockPrice>").append(StockData.getInstance().calculate(i)).append("</stockPrice>");
                sb.append("<stockImage>").append(stock.image()).append("</stockImage>");
                sb.append("</stockData>");
                i += 1;
            }
            sb.append("</stockDataList>");
            webSocket.writeTextMessage(sb.toString());
        });
    }

}
