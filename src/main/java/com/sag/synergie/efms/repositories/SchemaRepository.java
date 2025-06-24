/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.sag.synergie.efms.repositories;

import com.sag.synergie.efms.model.Schema;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

public interface SchemaRepository extends MongoRepository<Schema, URI> {
    public boolean existsByUuid(UUID uuid);
    public Optional<Schema> findByUuid(UUID uuid);
    public Optional<Schema> deleteByUuid(UUID uuid);
}