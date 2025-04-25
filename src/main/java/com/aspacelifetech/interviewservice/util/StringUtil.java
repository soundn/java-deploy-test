
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

package com.aspacelifetech.interviewservice.util;

/**
 * @Author: Idris Ishaq
 * @Date: 24 Nov, 2024
 */


public class StringUtil {

    public static String getEnvironmentVariable(String value, String defaultValue) {
        return System.getenv(value) == null ? defaultValue : System.getenv(value);
    }

    public static String of(Object obj) {
        try {
            return obj.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static Integer parseInteger(Object obj) {
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public static Long parseLong(Object obj) {
        try {
            return Long.parseLong(obj.toString());
        } catch (Exception e) {
            return 0L;
        }
    }

    public static Boolean parseBoolean(Object obj) {
        try {
            return Boolean.parseBoolean(obj.toString());
        } catch (Exception e) {
            return false;
        }
    }

    public static Double parseDouble(Object obj) {
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
            return 0D;
        }
    }

}
