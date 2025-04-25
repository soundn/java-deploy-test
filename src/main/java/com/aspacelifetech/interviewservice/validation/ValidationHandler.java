package com.aspacelifetech.interviewservice.validation;

import com.aspacelifetech.interviewservice.config.ConfigData;
import com.aspacelifetech.interviewservice.handlers.ResponseObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

/**
 * @Author: Idris Ishaq
 * @Date: 11 Sep, 2023
 */


public class ValidationHandler {

    private static final String MSG = "Constraint Data Violation";

    private ValidationHandler() {
    }

    public static ValidationHandler getInstance() {
        return ValidationHandlerInit.INSTANCE;
    }

    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }


    public <T> void reportError(RoutingContext ctx, Set<ConstraintViolation<T>> validate) {

        JsonArray jsonArray = build(validate);

        JsonObject jsonObject = new JsonObject();
        jsonObject.put(ResponseObject.Message, MSG);
        jsonObject.put(ResponseObject.StatusCode, 400);
        jsonObject.put(ResponseObject.GatewayResponse, MSG);
        jsonObject.put(ResponseObject.Success, false);
        jsonObject.put(ResponseObject.Status, "fail");
        jsonObject.put(ResponseObject.IsConstraintError, true);
        jsonObject.put(ResponseObject.ConstraintError, jsonArray);

        ctx.response().setStatusCode(400)
                .putHeader(ConfigData.CONTENT_TYPE, ConfigData.JSON_CONTENT)
                .send(jsonObject.encode());
    }

    public <T> JsonObject reportError(Set<ConstraintViolation<T>> validate) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put(ResponseObject.ConstraintError, build(validate));
        return jsonObject;
    }

    private <T> JsonArray build(Set<ConstraintViolation<T>> validate) {
        JsonArray jsonArray = new JsonArray();
        for (ConstraintViolation<T> constraint : validate) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put(constraint.getPropertyPath().toString(), constraint.getMessage());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private static final class ValidationHandlerInit {
        private static final ValidationHandler INSTANCE = new ValidationHandler();
    }

}
