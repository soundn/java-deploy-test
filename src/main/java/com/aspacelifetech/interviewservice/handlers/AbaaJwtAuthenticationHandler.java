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

package com.aspacelifetech.interviewservice.handlers;

import com.aspacelifetech.interviewservice.config.AbaaJwtSigningCertificateBuilder;
import com.aspacelifetech.interviewservice.config.ConfigData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @Author: Idris Ishaq
 * @Date: 01 Oct, 2024
 */


@Slf4j
@Service
@AllArgsConstructor
public class AbaaJwtAuthenticationHandler {


    public CompletableFuture<String> authenticate(String jwe) {
        CompletableFuture<String> future = new CompletableFuture<>();
        try {

            JwtParserBuilder builder = Jwts.parser();
            builder.decryptWith(AbaaJwtSigningCertificateBuilder.getPrivateKey());
            builder.requireIssuer(ConfigData.ISSUER_NAME);
            JwtParser jwtParser = builder.build();

            Claims claims = jwtParser.parseEncryptedClaims(jwe).getPayload();

            future.complete(claims.getSubject());
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

}
