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
import org.springframework.data.annotation.Id;

import java.net.URI;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class Schema {
    @Id
    private URI uri;
    private String name;
    private String version;
    private String schemaJson;
    private boolean deprecated;
    private UUID uuid;
}
