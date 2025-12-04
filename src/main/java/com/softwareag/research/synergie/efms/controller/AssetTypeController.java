/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.softwareag.research.synergie.efms.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.softwareag.research.synergie.efms.model.AssetType;
import com.softwareag.research.synergie.efms.model.SchemaAssetTypeAssignment;
import com.softwareag.research.synergie.efms.services.AssetTypeService;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/efms")
public class AssetTypeController {
    private AssetTypeService assetTypeService;

    public AssetTypeController(AssetTypeService assetTypeService) {
        this.assetTypeService = assetTypeService;
    }

    @GetMapping(value="/assettypes")
    public List<AssetType> getAssetTypes() {
        return assetTypeService.getAssetTypes();
    }

    @GetMapping(value="/assettypes/{assetTypeName}")
    public ResponseEntity<AssetType> getAssetType(@PathVariable("assetTypeName")String assetTypeName) {
        Optional<AssetType> assetType = assetTypeService.getAssetType(assetTypeName);
        if (assetType.isPresent())
            return ResponseEntity.ok(assetType.get());
        else return ResponseEntity.notFound().build();
    }

    @PostMapping(value="/assettypes")
    public ResponseEntity<Void> createAssetType(
            @RequestBody AssetType assetType,
            HttpServletRequest request) {
        String userId = null!=request.getUserPrincipal()?request.getUserPrincipal().getName():"default";
        Optional<AssetType> createdAssetType = assetTypeService.addAssetType(assetType, userId);
        if (createdAssetType.isPresent()) {
            URI uri = URI.create(request.getRequestURI() + "/" + createdAssetType.get().getName());
            return ResponseEntity.created(uri).build();
        }
        else return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PutMapping(value = "/assettypes/{assetTypeName}")
    public ResponseEntity<Void> updateAssetType(
            @PathVariable("assetTypeName")String assetTypeName,
            @RequestBody AssetType assetType,
            HttpServletRequest request) {
        if (!assetTypeName.equals(assetType.getName()))
            return ResponseEntity.badRequest().build();

        String userId = null!=request.getUserPrincipal()?request.getUserPrincipal().getName():"default";
        Optional<AssetType> updatedAssetType = assetTypeService.updateAssetType(
                assetType,
                userId);

        if (updatedAssetType.isPresent()) {
            URI uri = URI.create(request.getRequestURI() + "/" +
                    updatedAssetType.get().getName());
            return ResponseEntity.noContent().build();
        }
        else return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @DeleteMapping(value="/assettypes/{assetType}")
    public ResponseEntity<Void> deleteAssetType(@PathVariable("assetType") String assetType,
                                                       Principal principal) {
        if (assetTypeService.deleteAssetType(assetType,
                null!=principal?principal.getName():"default"))
            return ResponseEntity.noContent().build();
        else return ResponseEntity.notFound().build();
    }

    @GetMapping(value="/sata")
    public List<SchemaAssetTypeAssignment> getSchemaAssetTypeAssignments() {
        return assetTypeService.getSchemaAssetTypeAssignments();
    }

    @GetMapping(value="/sata/{sataId}")
    public ResponseEntity<SchemaAssetTypeAssignment> getSchemaAssetTypeAssignments(
            @PathVariable("sataId")UUID sataId) {
        Optional<SchemaAssetTypeAssignment> sata =
                assetTypeService.getSchemaAssetTypeAssignment(sataId);

        if (sata.isPresent())
            return ResponseEntity.ok(sata.get());
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value="/sata")
    public ResponseEntity<SchemaAssetTypeAssignment> addSchemaAssetTypeAssignment(
            @RequestBody SchemaAssetTypeAssignment sata) {
        try {
            Optional<SchemaAssetTypeAssignment> created =
                    assetTypeService.addSchemaAssignment(sata);
            if (created.isPresent())
                return ResponseEntity.ok(created.get());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch(AssetTypeService.UnknownAssetTypeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value="/sata/{sataId}")
    public ResponseEntity<Void> deleteSchemaAssetTypeAssignment(
            @PathVariable("sataId") UUID sataId) {
        if (assetTypeService.deleteSchemaAssignment(sataId))
            return ResponseEntity.noContent().build();
        else return ResponseEntity.notFound().build();
    }
}