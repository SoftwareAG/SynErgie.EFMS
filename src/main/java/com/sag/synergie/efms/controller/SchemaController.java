/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.sag.synergie.efms.controller;

import com.sag.synergie.efms.model.Schema;
import com.sag.synergie.efms.model.SchemaDto;
import com.sag.synergie.efms.services.SchemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@Controller
@RequestMapping("/efms/schemas")
public class SchemaController {

    private SchemaService schemaService;

    public SchemaController(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @GetMapping
    @Operation(summary="Get a list of all registered schemas. " +
            "Registering schemas is required in order to use respective flexibility instances.")
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200", description = "List of all registered schemas",
                content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        array = @ArraySchema(schema = @io.swagger.v3.oas.annotations.media.Schema(
                                implementation = Schema.class)))
                })
    })
    public ResponseEntity<List<Schema>> getSchemas() {
        return ResponseEntity.ok(schemaService.get());
    }

    @GetMapping(value="/{uuid}")
    @Operation(summary="Get a schema by its UUID.")
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200", description = "Schema properties",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = Schema.class))
                    }),
            @ApiResponse(responseCode = "404", description = "no schema of that UUID found",
                    content = @Content)
    })
    public ResponseEntity<Schema> getSchema(@PathVariable("uuid") UUID uuid) {
        Optional<Schema> schema = schemaService.getByUuid(uuid);
        if (schema.isPresent())
            return ResponseEntity.ok(schema.get());
        else return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary="Registers a new schema.")
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200", description = "Schema properties",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = Schema.class))
                    }),
            @ApiResponse(responseCode = "404", description = "no schema of that UUID found",
                    content = @Content)
    })
    public ResponseEntity<Void> registerSchema(
            @Parameter(content = @Content(
                        schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = SchemaDto.class),
                        mediaType = MediaType.APPLICATION_JSON_VALUE),
                        required = true)
            @RequestBody SchemaDto schema,
            HttpServletRequest request) {
        if (schemaService.isSupported(schema.getUri()))
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        Optional<Schema> os = schemaService.register(schema);
        if (os.isPresent()) {
            URI uri = URI.create(request.getRequestURI() + "/" + os.get().getUuid());
            return ResponseEntity.created(uri).build();
        }
        else return ResponseEntity.badRequest().build();
    }

    @DeleteMapping(value="/{uuid}")
    public ResponseEntity<Void> deleteSchema(@PathVariable("uuid") UUID uuid) {
        if (schemaService.getByUuid(uuid).isEmpty())
            return ResponseEntity.notFound().build();

        if (schemaService.deleteByUuid(uuid))
            return ResponseEntity.noContent().build();
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}