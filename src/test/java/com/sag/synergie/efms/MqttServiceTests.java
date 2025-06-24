/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.sag.synergie.efms;

import com.sag.synergie.efms.model.MqttClientConfig;
import com.sag.synergie.efms.services.MqttClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MqttServiceTests {

    @Autowired
    private MqttClientService mqttClientService;

    @Test
    public void testPublish() {
        String clientId = "";
        MqttClientConfig clientConfig = new MqttClientConfig(
                clientId,
                "",
                "",
                "",
                "");

        mqttClientService.update(clientConfig);

        assertTrue(mqttClientService.publish(clientId,
                "TestTopic",
                "Hello world",
                1));

    }
}
