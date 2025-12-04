/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.softwareag.research.synergie.efms.services;

import org.springframework.stereotype.Service;

import com.softwareag.research.synergie.efms.model.FlexibilityAsset;
import com.softwareag.research.synergie.efms.model.event.AssetCreatedEvent;
import com.softwareag.research.synergie.efms.model.event.AssetDeletedEvent;
import com.softwareag.research.synergie.efms.repositories.AssetRepository;
import com.softwareag.research.synergie.efms.repositories.AssetTypeRepository;
import com.softwareag.research.synergie.efms.repositories.SchemaAssetTypeAssignmentRepository;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AssetService {
    private AssetRepository assetRepository;
    private SchemaService schemaService;
    private AssetTypeService assetTypeService;
    private SchemaAssetTypeAssignmentRepository schemaAssetTypeAssignmentRepository;
    private EventService eventService;

    public AssetService(AssetRepository assetRepository,
                        SchemaService schemaService,
                        AssetTypeService assetTypeService,
                        SchemaAssetTypeAssignmentRepository schemaAssetTypeAssignmentRepository,
                        EventService eventService) {
        this.assetRepository = assetRepository;
        this.schemaService = schemaService;
        this.assetTypeService = assetTypeService;
        this.schemaAssetTypeAssignmentRepository = schemaAssetTypeAssignmentRepository;
        this.eventService = eventService;
    }

    public List<FlexibilityAsset> getFlexibilityAssets() {
        return assetRepository.findAll();
    }

    public Optional<FlexibilityAsset> getById(UUID id) {
        return assetRepository.findById(id);
    }

    public boolean deleteById(UUID id, String userId) {
        if (!assetRepository.existsById(id))
            return false;
        FlexibilityAsset asset = assetRepository.findById(id).get();
        assetRepository.deleteById(id);
        eventService.publish(new AssetDeletedEvent(asset, userId));
        return true;
    }
    public List<FlexibilityAsset> getByAssetType(String assetType) {
        return assetRepository.findBySchemaIn(
                assetTypeService.getAssignedSchemas(assetType)
        );
    }

    public FlexibilityAsset createAsset(UUID id, URI schemaUri,
                                        String json, String userId) {
        // make sure we don't overwrite existing assets
        if (assetRepository.existsById(id))
            throw new AssetExistsException();

        FlexibilityAsset asset = new FlexibilityAsset(schemaUri, json);
        asset.setId(id);
        asset.setLastChange(Instant.now());
        asset.setUserId(userId);
        asset = assetRepository.save(asset);
        // send event
        eventService.publish(new AssetCreatedEvent(asset, userId));
        return asset;
    }

    public class AssetExistsException extends RuntimeException {}

}