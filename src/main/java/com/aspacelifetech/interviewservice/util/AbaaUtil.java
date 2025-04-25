
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Idris Ishaq
 * @Date: 24 Nov, 2024
 */


public class AbaaUtil {

    private static final String REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public static boolean isValidEmail(String email) {
        // Compile the regular expression into a pattern
        Pattern pattern = Pattern.compile(REGEX);
        // Create a matcher to check the email against the pattern
        Matcher matcher = pattern.matcher(email);
        // Return whether the email matches the regex pattern
        return matcher.matches();
    }

}
