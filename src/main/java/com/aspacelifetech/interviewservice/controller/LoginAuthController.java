
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
import com.aspacelifetech.interviewservice.config.WebConfig;
import com.aspacelifetech.interviewservice.handlers.CookieHandler;
import com.aspacelifetech.interviewservice.handlers.DateTimeHandler;
import com.aspacelifetech.interviewservice.handlers.ErrorResponseUtil;
import com.aspacelifetech.interviewservice.handlers.JwtHandler;
import com.aspacelifetech.interviewservice.model.AuthData;
import com.aspacelifetech.interviewservice.model.UserDataInfo;
import com.aspacelifetech.interviewservice.validation.ValidationHandler;
import io.vertx.core.Handler;
import io.vertx.core.http.Cookie;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import jakarta.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Set;
import java.util.UUID;

/**
 * @Author: Idris Ishaq
 * @Date: 05 Dec, 2024
 */


@Controller
public class LoginAuthController implements Handler<RoutingContext> {

    /* private final String cookieDomain; */
    private final JwtHandler jwtHandler;

    @Autowired
    public LoginAuthController(Router router, JwtHandler jwtHandler, WebConfig config) {
        this.jwtHandler = jwtHandler;
        /* this.cookieDomain = config.getCookieDomain(); */
        router.post("/auth_login")
                .consumes(ConfigData.JSON_CONTENT)
                .handler(BodyHandler.create(false))
                .handler(this);
    }


    @Override
    public void handle(RoutingContext ctx) {
        try {

            AuthData authData = ctx.body().asPojo(AuthData.class);

            Set<ConstraintViolation<AuthData>> validate = ValidationHandler.getInstance().validator().validate(authData);
            if (validate.isEmpty()) {
                processAuthHandler(authData, ctx);
            } else {
                ValidationHandler.getInstance().reportError(ctx, validate);
            }
        } catch (Exception e) {
            ErrorResponseUtil.doBuild(ctx, e);
        }
    }

    private void processAuthHandler(AuthData authData, RoutingContext ctx) {
        if (authData.getUsername().equalsIgnoreCase("webAuth") && authData.getPassword().contentEquals("webAuth@101")) {

            jwtHandler.buildToken(UserDataInfo.ID, UUID.randomUUID().toString())
                    .whenCompleteAsync((jwe, throwable) -> {
                        if (throwable != null) {
                            ErrorResponseUtil.doBuild(ctx, "Invalid Username Or Password", "Auth Login Request not Successful");
                        } else {

                            JsonObject userDetails = new JsonObject();
                            userDetails.put("userId", UserDataInfo.ID);
                            userDetails.put("fullName", UserDataInfo.NAME);
                            userDetails.put("jobPosition", UserDataInfo.JOB_POSITION);
                            userDetails.put("loginDate", DateTimeHandler.getDate());
                            userDetails.put("loginTime", DateTimeHandler.getTime());

                            JsonObject json = new JsonObject();
                            json.put("success", true);
                            json.put("statusCode", 200);
                            json.put("message", "Authentication was Successful");
                            json.put("dataSuccess", new JsonObject()
                                    .put("gatewayResponse", "Login Request was Successful")
                                    .put("userDetails", userDetails));

                            Cookie cookie = CookieHandler.createCookie("_auth_cookie", jwe);
                            ctx.response()
                                    .setStatusCode(200)
                                    .addCookie(cookie)
                                    .putHeader(ConfigData.CONTENT_TYPE, ConfigData.JSON_CONTENT)
                                    .send(json.encode());
                        }
                    });
        } else {
            ErrorResponseUtil.doBuild(ctx, "Invalid Username Or Password", "Auth Login Request not Successful");
        }
    }

}
