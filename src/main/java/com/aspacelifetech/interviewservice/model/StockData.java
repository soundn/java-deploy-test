
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

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @Author: Idris Ishaq
 * @Date: 05 Dec, 2024
 */


public class StockData {

    private static final SecureRandom secureRandom = new SecureRandom();

    private final Set<Stock> STOCK_DATA;
    private final String[] DAYS;

    private StockData() {
        this.STOCK_DATA = new HashSet<>();
        STOCK_DATA.add(Stock.create("BTC", "https://interview.abaaapps.com:8185/bit_coin.png"));
        STOCK_DATA.add(Stock.create("ETH", "https://interview.abaaapps.com:8185/eth_coin.png"));
        STOCK_DATA.add(Stock.create("LIGHT", "https://interview.abaaapps.com:8185/light_coin.png"));
        STOCK_DATA.add(Stock.create("XRP", "https://interview.abaaapps.com:8185/xrp_coin.png"));

        this.DAYS = new String[]{"Mon", "Tue", "Web", "Thu", "Fri", "Sat", "Sun"};
    }

    public static StockData getInstance() {
        return StockDataInit.INSTANCE;
    }

    public Set<Stock> getStockData() {
        return STOCK_DATA;
    }

    public String[] getDays() {
        return DAYS;
    }

    public int calculate(int i) {
        return (secureRandom.nextInt(i) + 1) * i + (i * i);
    }

    private static final class StockDataInit {
        private static final StockData INSTANCE = new StockData();
    }

    public record Stock(String stockId, String stockName, String image) {
        public static Stock create(String stock, String image) {
            return new Stock(UUID.randomUUID().toString(), stock, image);
        }
    }

}
