/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.sag.synergie.efms.model.event;

import java.time.Instant;
import java.util.UUID;

public interface Event {
    public UUID getId();
    public String getOrigin();
    public Instant getTimestamp();
}
