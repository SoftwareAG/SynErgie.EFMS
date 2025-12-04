/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.softwareag.research.synergie.efms.model.event;

import java.util.Optional;

import com.softwareag.research.synergie.efms.model.FlexibilityAsset;

public class AssetUpdatedEvent extends BaseEvent {
    private FlexibilityAsset before;
    private FlexibilityAsset after;

    public AssetUpdatedEvent(FlexibilityAsset before, FlexibilityAsset after, String origin) {
        super(origin);
        this.before = before;
        this.after = after;
    }
}
