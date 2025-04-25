
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Idris Ishaq
 * @Date: 30 Sep, 2024
 */


public class AbaaJwtSigningCertificateBuilder {

    private static final String ALGORITHM = "RSA";

    private static final String privateKeyPath = "pem/rsaPrivateKey.pem";
    private static final String publicKeyPath = "pem/rsaPublicKey.pem";

    private static final Pattern BEGIN_PATTERN = Pattern.compile("-----BEGIN ([A-Z ]+)-----");
    private static final Pattern END_PATTERN = Pattern.compile("-----END ([A-Z ]+)-----");


    public static PublicKey getPublicKey() throws Exception {
        String jwtPublicKey = loadCertificate(publicKeyPath);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        byte[] keyBytes = Base64.getDecoder().decode(jwtPublicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }

    public static PrivateKey getPrivateKey() throws Exception {
        String jwtPrivateKey = loadCertificate(privateKeyPath);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        byte[] keyBytes = Base64.getDecoder().decode(jwtPrivateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    private static String loadCertificate(String path) throws Exception {
        InputStream inputStream = AbaaJwtSigningCertificateBuilder.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new IllegalArgumentException("Invalid certificate path");
        }
        StringBuilder sb = new StringBuilder();
        Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        int c;
        while ((c = reader.read()) != -1) {
            sb.append((char) (c & 0xff));
        }
        return loadCertificateContent(sb.toString());
    }

    private static String loadCertificateContent(String pem) {

        Matcher beginMatcher = BEGIN_PATTERN.matcher(pem);
        Matcher endMatcher = END_PATTERN.matcher(pem);
        StringBuilder sb = new StringBuilder();

        while (true) {
            boolean begin = beginMatcher.find();
            if (!begin) {
                break;
            }
            String beginDelimiter = beginMatcher.group(1);
            boolean end = endMatcher.find();
            if (!end) {
                throw new RuntimeException("Missing -----END " + beginDelimiter + "----- delimiter");
            } else {
                String endDelimiter = endMatcher.group(1);
                if (!beginDelimiter.equals(endDelimiter)) {
                    throw new RuntimeException("Missing -----END " + beginDelimiter + "----- delimiter");
                } else {
                    String content = pem.substring(beginMatcher.end(), endMatcher.start());
                    content = content.replaceAll("\\s", "");
                    if (content.isEmpty()) {
                        throw new RuntimeException("Empty pem file");
                    }
                    sb.append(content);

                }
            }
        }
        return sb.toString();
    }

}
