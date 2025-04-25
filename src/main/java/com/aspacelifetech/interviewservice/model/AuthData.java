
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

package com.aspacelifetech.interviewservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author: Idris Ishaq
 * @Date: 05 Dec, 2024
 */


@Data
public class AuthData {
    @NotNull(message = "Username Cannot be Null")
    @NotBlank(message = "Username Cannot be Blank")
    @NotEmpty(message = "Username Cannot be Empty")
    private String username;
    @NotNull(message = "Password Cannot be Null")
    @NotBlank(message = "Password Cannot be Blank")
    @NotEmpty(message = "Password Cannot be Empty")
    private String password;
}
