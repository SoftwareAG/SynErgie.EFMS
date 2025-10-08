/*
* SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
*
* SPDX-License-Identifier: Apache-2.0
*
*/


package com.sag.synergie.efms.controller;

import com.sag.synergie.efms.model.FlexibilityAsset;
import com.sag.synergie.efms.model.SchemaAssetTypeAssignment;
import com.sag.synergie.efms.services.AssetTypeService;
import com.sag.synergie.efms.services.AssetService;
import com.sag.synergie.efms.services.SchemaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/efms/assets")
public class AssetController {
    private AssetService assetService;
    private AssetTypeService assetTypeService;
    private SchemaService schemaService;

    public AssetController(AssetService assetService,
                           AssetTypeService assetTypeService,
                           SchemaService schemaService) {
        this.assetService = assetService;
        this.assetTypeService = assetTypeService;
        this.schemaService = schemaService;
    }

    @GetMapping
    public List<FlexibilityAsset> getFlexibilityAssets() {
        return assetService.getFlexibilityAssets();
    }

    @GetMapping(value="/json")
    public List<String> getFlexibilityAssetsAsJson() {
        return assetService.getFlexibilityAssets()
                .parallelStream()
                .map(m->m.getAssetJson())
                .collect(Collectors.toList());
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<FlexibilityAsset> getFlexibilityAssetById(
            @PathVariable UUID id) {
        Optional<FlexibilityAsset> asset = assetService.getById(id);
        if (asset.isPresent())
            return ResponseEntity.ok(asset.get());
        return ResponseEntity.notFound().build();
    }
    @GetMapping(value="/{id}/json")
    public ResponseEntity<String> getFlexibilityAssetByIdAsJson(
            @PathVariable UUID id) {
        Optional<FlexibilityAsset> asset = assetService.getById(id);
        if (asset.isPresent())
            return ResponseEntity.ok(asset.get().getAssetJson());
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> deleteFlexibilityAssetById(
            @PathVariable UUID id,
            HttpServletRequest request) {
        String userId = null!=request.getUserPrincipal()?request.getUserPrincipal().getName():"default";
        if (assetService.deleteById(id, userId))
            return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value="/type/{assetType}")
    public List<FlexibilityAsset> getFlexibilityAssetsByType(
            @PathVariable String assetType) {
        return assetService.getByAssetType(assetType);
    }

    @GetMapping(value="/type/{assetType}/json")
    public List<String> getFlexibilityAssetsByTypeAsJson(
            @PathVariable String assetType) {
        return assetService.getByAssetType(assetType)
                .parallelStream()
                .map(m->m.getAssetJson())
                .collect(Collectors.toList());
    }

    // we're enforcing that the flexibility provider takes care of ID generation
    @PostMapping(value="/type/{assetType}/{assetId}")
    public ResponseEntity<FlexibilityAsset> getFlexibilityAssetsByType(
            @PathVariable("assetType") String assetTypeName,
            @PathVariable UUID assetId,
            @RequestBody String assetJson,
            HttpServletRequest request) {
        if (!assetTypeService.hasAssetType(assetTypeName))
            return ResponseEntity.badRequest().build();

        // for an asset type, multiple schemas might be assigned (i.e. flexibilityspace -> v04, v10)
        // get schemas to test the asset for
        List<SchemaAssetTypeAssignment> assignments =
                assetTypeService.getSchemaAssignments(assetTypeName);
        Optional<URI> matchingSchema = Optional.empty();
        for (SchemaAssetTypeAssignment a: assignments) {
            if (schemaService.isValid(a.getSchema(), assetJson)) {
                // matching schema found
                matchingSchema = Optional.of(a.getSchema());
                break;
            }
        }
        if (matchingSchema.isEmpty()) // no matching schema found
            return ResponseEntity.badRequest().build();

        // schema found, create asset
        String userId = null!=request.getUserPrincipal()?request.getUserPrincipal().getName():"default";
        try {
            FlexibilityAsset asset = assetService.createAsset(assetId,
                    matchingSchema.get(), assetJson, userId);
            URI uri = URI.create(request.getRequestURI() + "/" + asset.getId());
            return ResponseEntity.created(uri).build();
        } catch(AssetService.AssetExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}