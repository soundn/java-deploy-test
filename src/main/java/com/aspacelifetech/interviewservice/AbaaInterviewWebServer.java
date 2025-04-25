
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

package com.aspacelifetech.interviewservice;

import com.aspacelifetech.interviewservice.config.AbaaApplicationConfigProfileListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Idris Ishaq
 * @Date: 05 Dec, 2024
 */


@SpringBootApplication(scanBasePackages = "com.aspacelifetech.interviewservice.*")
public class AbaaInterviewWebServer {


    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AbaaInterviewWebServer.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.addListeners(new AbaaApplicationConfigProfileListener());
        springApplication.run(args);
    }

}