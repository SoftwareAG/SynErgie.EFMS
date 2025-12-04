/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.softwareag.research.synergie.efms.model.event;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseEvent implements Event {
    protected UUID id;
    protected String origin;
    protected Instant timestamp;

    public BaseEvent(String origin) {
        this.origin = origin;
        id = UUID.randomUUID();
        timestamp = Instant.now();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getOrigin() {
        return null;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
