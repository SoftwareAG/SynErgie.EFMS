/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.softwareag.research.synergie.efms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.net.URI;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class SchemaAssetTypeAssignment {
    @Id
    private UUID id;
    private String description;
    private URI schema;
    private String assetType;
}