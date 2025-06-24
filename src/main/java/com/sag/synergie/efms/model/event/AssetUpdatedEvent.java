/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.sag.synergie.efms.model.event;

import com.sag.synergie.efms.model.FlexibilityAsset;

import java.util.Optional;

public class AssetUpdatedEvent extends BaseEvent {
    private FlexibilityAsset before;
    private FlexibilityAsset after;

    public AssetUpdatedEvent(FlexibilityAsset before, FlexibilityAsset after, String origin) {
        super(origin);
        this.before = before;
        this.after = after;
    }
}
