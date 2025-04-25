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

package com.aspacelifetech.interviewservice.config;

import com.aspacelifetech.interviewservice.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @Author: Idris Ishaq
 * @Date: 20 Jul, 2024
 */


@Slf4j
public class AbaaApplicationConfigProfileListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        String profile = StringUtil.getEnvironmentVariable("SPRING_PROFILES_ACTIVE", "prod");
        ConfigurableEnvironment environment = event.getEnvironment();
        environment.setActiveProfiles(profile);
    }

}
