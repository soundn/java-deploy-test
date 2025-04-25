/*
 * Copyright 2023 The AbaaChatPay Project
 *
 * The AbaaChatPay Project licenses this file to you under the AbaaTech Solution License,
 * version 1.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.abaachatpay.com/licenses/LICENSE-1.0
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
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.AeadAlgorithm;
import io.jsonwebtoken.security.KeyAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: Idris Ishaq
 * @Date: 20 Feb, 2024
 */


@Service
@AllArgsConstructor
public class JwtHandler {

    private static final AeadAlgorithm AEAD_ALGORITHM = Jwts.ENC.A256GCM;
    private static final KeyAlgorithm<PublicKey, PrivateKey> KEY_ALGORITHM = Jwts.KEY.RSA_OAEP_256;


    public CompletableFuture<String> buildToken(String subject, String sessionId) {
        CompletableFuture<String> future = new CompletableFuture<>();
        try {

            JwtBuilder builder = Jwts.builder();
            builder.subject(subject);
            builder.issuedAt(Date.from(DateTimeHandler.getDateInstant()));
            builder.claim("sessionId", sessionId);
            /* token expiration time */
            builder.expiration(Date.from(getDateInstant()));
            /* encrypt with public key */
            builder.encryptWith(AbaaJwtSigningCertificateBuilder.getPublicKey(), KEY_ALGORITHM, AEAD_ALGORITHM);
            /* audience */
            builder.audience().add(ConfigData.ISSUER_NAME);
            /* issuer name */
            builder.issuer(ConfigData.ISSUER_NAME);
            builder.claim("SESSION-REF", TokenGenerator.generateToken(23));

            future.complete(builder.compact());
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    private Instant getDateInstant() {
        LocalDate localDate = DateTimeHandler.getLocalDate().plusDays(5);
        return localDate.atStartOfDay().atZone(DateTimeHandler.getZoneId().toZoneId()).toInstant();
    }

}
