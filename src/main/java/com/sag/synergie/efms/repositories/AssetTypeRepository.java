/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.sag.synergie.efms.repositories;

import com.sag.synergie.efms.model.AssetType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface AssetTypeRepository extends MongoRepository<AssetType, UUID> {
    public Optional<AssetType> findByName(String name);
    public boolean existsByName(String name);
    public void deleteByName(String name);
}
