/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.softwareag.research.synergie.efms.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.softwareag.research.synergie.efms.model.FlexibilityAsset;

import java.util.List;
import java.util.UUID;
import java.net.URI;

public interface AssetRepository extends MongoRepository<FlexibilityAsset, UUID> {
    public List<FlexibilityAsset> findBySchemaIn(List<URI> schemas);
}
