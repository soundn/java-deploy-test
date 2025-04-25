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

import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author: Idris Ishaq
 * @Date: 03 Feb, 2024
 */


public class TokenGenerator {

    private final static char[] arrayChar = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

    private static String generate(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = arrayChar[v >>> 4];
            hexChars[j * 2 + 1] = arrayChar[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String generateToken(int len) {
        byte[] bytes = new byte[len];
        ThreadLocalRandom.current().nextBytes(bytes);
        return generate(bytes);
    }

    public static String generateToken() {
        return generateToken(19);
    }

}
