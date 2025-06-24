/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.sag.synergie.efms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;

@AllArgsConstructor
@Getter
@Setter
public class FlexibilityAsset extends BaseAsset {
    private URI schema;
    //private String assetId;
    private String assetJson;
}
