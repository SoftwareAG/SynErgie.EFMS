/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.sag.synergie.efms.services;

import com.sag.synergie.efms.model.AssetType;
import com.sag.synergie.efms.model.SchemaAssetTypeAssignment;
import com.sag.synergie.efms.repositories.AssetTypeRepository;
import com.sag.synergie.efms.repositories.SchemaAssetTypeAssignmentRepository;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AssetTypeService {
    private AssetTypeRepository assetTypeRepository;
    private SchemaAssetTypeAssignmentRepository sataRepository;

    public AssetTypeService(AssetTypeRepository assetTypeRepository,
                            SchemaAssetTypeAssignmentRepository sataRepository) {
        this.assetTypeRepository = assetTypeRepository;
        this.sataRepository = sataRepository;
    }

    public List<AssetType> getAssetTypes() {
        return assetTypeRepository.findAll();
    }

    public boolean hasAssetType(String assetType) {
        return assetTypeRepository.existsByName(assetType);
    }

    public Optional<AssetType> getAssetType(String name) {
        return assetTypeRepository.findByName(name);
    }

    public Optional<AssetType> addAssetType(AssetType assetType, String userId) {
        if (assetTypeRepository.existsByName(assetType.getName()))
            return Optional.empty();
        assetType.setUserId(userId);
        if (null == assetType.getId() || assetType.getId().isEmpty())
            assetType.setId(UUID.randomUUID().toString());
        if (null == assetType.getLastChange())
            assetType.setLastChange(Instant.now());
        // TODO: event
        return Optional.of(assetTypeRepository.save(assetType));
    }

    public Optional<AssetType> updateAssetType(AssetType assetType, String userId) {
        if (!assetTypeRepository.existsByName(assetType.getName()))
            return Optional.empty();
        AssetType existing = assetTypeRepository.findByName(assetType.getName()).get();
        existing.setDescription(assetType.getDescription());
        existing.setUserId(userId);
        existing.setLastChange(Instant.now());
        // TODO: event
        return Optional.of(assetTypeRepository.save(existing));
    }

    public boolean deleteAssetType(String name, String userId) {
        if (!assetTypeRepository.existsByName(name))
            return false;
        assetTypeRepository.deleteByName(name);
        // TODO: event
        return true;
    }

    public Optional<SchemaAssetTypeAssignment> addSchemaAssignment(
            SchemaAssetTypeAssignment sata) throws UnknownAssetTypeException {
        if ((null != sata.getId() && sataRepository.existsById(sata.getId())) ||
            sataRepository.existsBySchemaAndAssetType(sata.getSchema(), sata.getAssetType()))
            return Optional.empty();

        // TODO: check if schema exists?

        // check if asset type exists
        if (!assetTypeRepository.existsByName(sata.getAssetType()))
            throw new UnknownAssetTypeException();

        if (null == sata.getId())
            sata.setId(UUID.randomUUID());
        sata = sataRepository.save(sata);
        return Optional.of(sata);
    }

    public boolean deleteSchemaAssignment(UUID id) {
        if (!sataRepository.existsById(id))
            return false;
        sataRepository.deleteById(id);
        return true;
    }

    public List<URI> getAssignedSchemas(String assetType) {
        List<SchemaAssetTypeAssignment> assignments = sataRepository.findByAssetType(assetType);
        return assignments.parallelStream()
                .map(a->a.getSchema())
                .collect(Collectors.toList());
    }

    public List<SchemaAssetTypeAssignment> getSchemaAssetTypeAssignments() {
        return sataRepository.findAll();
    }

    public List<SchemaAssetTypeAssignment> getSchemaAssignments(String assetType) {
        return sataRepository.findByAssetType(assetType);
    }

    public Optional<SchemaAssetTypeAssignment> getSchemaAssetTypeAssignment(
            UUID sataId) {
        return sataRepository.findById(sataId);
    }

    public class UnknownAssetTypeException extends RuntimeException {}
}
