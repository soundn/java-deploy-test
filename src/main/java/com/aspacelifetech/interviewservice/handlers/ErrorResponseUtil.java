package com.aspacelifetech.interviewservice.handlers;


import com.aspacelifetech.interviewservice.config.ConfigData;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.net.ConnectException;

/**
 * @Author: Idris Ishaq
 * @Date: 04 Jan, 2024
 */


public class ErrorResponseUtil {

    private static final String MGS = "Error: Unable to Process Request!";

    public static String doBuild(int statusCode, String message, String gateway) {
        return doBuild(statusCode, message, gateway, null);
    }

    public static String doBuild(int statusCode, String message, String gateway, JsonObject jsonData) {

        if (jsonData == null) {
            jsonData = new JsonObject();
            jsonData.put("date", DateTimeHandler.getDate());
            jsonData.put("time", DateTimeHandler.getTime());
        }
        jsonData.put(ResponseObject.GatewayResponse, gateway);

        JsonObject jsonObject = new JsonObject();
        jsonObject.put(ResponseObject.Success, false);
        jsonObject.put(ResponseObject.Message, message);
        jsonObject.put(ResponseObject.StatusCode, statusCode);
        jsonObject.put(ResponseObject.IsConstraintError, false);
        jsonObject.put(ResponseObject.DataError, jsonData);
        jsonObject.put(ResponseObject.Status, "fail");
        return jsonObject.toString();
    }

    public static String doBuild(String message, String gateway) {
        return doBuild(400, message, gateway);
    }

    public static String doBuild(String message) {
        return doBuild(400, message, message);
    }

    public static void doBuild(RoutingContext ctx, String message, String gateway) {
        doBuild(ctx, 400, message, gateway);
    }

    public static void hashError(RoutingContext ctx) {
        doBuild(ctx, 400, "Invalid Ab8 Value", "Invalid Request");
    }

    public static void doBuild(RoutingContext ctx, int status, String message, String gateway) {
        ctx.response().setStatusCode(status)
                .putHeader(ConfigData.CONTENT_TYPE, ConfigData.JSON_CONTENT)
                .send(doBuild(status, message, gateway));
    }

    public static void doBuild(RoutingContext ctx, String message, int status, String gateway, JsonObject jsonObject) {
        ctx.response().setStatusCode(status)
                .putHeader(ConfigData.CONTENT_TYPE, ConfigData.JSON_CONTENT)
                .send(doBuild(status, message, gateway, jsonObject));
    }

    public static void doBuild(RoutingContext ctx, Exception e) {
        doBuild(ctx, e, 400);
    }

    public static void doBuild(RoutingContext ctx, Exception e, int code) {
        String msg = "";
        if (e instanceof ConnectException) {
            msg = "Error: Connection refused.";
        } /*else if (e instanceof StatusRuntimeException) {
            msg = MGS;
        }*/ else {
            msg = e.getMessage();
        }
        // log.error("ResponseError", e);
        ctx.response().setStatusCode(code)
                .putHeader(ConfigData.CONTENT_TYPE, ConfigData.JSON_CONTENT)
                .send(doBuild(code, msg, msg));
    }

    public static void doBuild(RoutingContext ctx, Throwable t) {
        String msg = "";
        String message = t.getMessage().toLowerCase();
        /* unavailable */
        if (message.startsWith("unavailable")) {
            msg = "Error: Unable to process request.";
        }
        /* unknown */
        else if (message.startsWith("unknown")) {
            msg = "Error: Unable to process request.";
        }
        /* aborted */
        else if (message.startsWith("aborted: java.lang.illegalargumentexception")) {
            msg = message.substring(45);
        }
        /* aborted */
        else if (message.startsWith("aborted")) {
            msg = message.substring(9);
        }
        /* else */
        else {
            msg = "Error: Connection refused.";
        }
        ctx.response().setStatusCode(400)
                .putHeader(ConfigData.CONTENT_TYPE, ConfigData.JSON_CONTENT)
                .send(doBuild(400, msg, MGS));
    }

}
