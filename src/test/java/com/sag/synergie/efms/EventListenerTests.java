/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.sag.synergie.efms;

import com.sag.synergie.efms.services.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EventListenerTests {

    private EventService eventService;

    public EventListenerTests(EventService eventService) {
        this.eventService = eventService;
    }
    @Test
    public void testMqttListener() {

    }
}
