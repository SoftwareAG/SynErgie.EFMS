/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.softwareag.research.synergie.efms.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.softwareag.research.synergie.efms.model.SchemaAssetTypeAssignment;

import java.net.URI;
import java.util.List;
import java.util.UUID;

public interface SchemaAssetTypeAssignmentRepository extends MongoRepository<SchemaAssetTypeAssignment, UUID> {
    public List<SchemaAssetTypeAssignment> findByAssetType(String assetType);
    public boolean existsBySchemaAndAssetType(URI schema, String assetType);
}
