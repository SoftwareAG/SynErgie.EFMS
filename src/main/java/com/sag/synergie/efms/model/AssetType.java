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

@AllArgsConstructor
@Getter
@Setter
public class AssetType extends BaseAsset {
    private String name;
    private String description;
}
