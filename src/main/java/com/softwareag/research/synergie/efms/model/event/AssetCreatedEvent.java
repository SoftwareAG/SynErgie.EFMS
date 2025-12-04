/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.softwareag.research.synergie.efms.model.event;

import com.softwareag.research.synergie.efms.model.FlexibilityAsset;

public class AssetCreatedEvent extends BaseEvent {
    private FlexibilityAsset asset;

    public AssetCreatedEvent(FlexibilityAsset asset, String origin) {
        super(origin);
        this.asset = asset;
    }
}
