/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.sag.synergie.efms.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.sag.synergie.efms.model.Schema;
import com.sag.synergie.efms.model.SchemaDto;
import com.sag.synergie.efms.repositories.SchemaRepository;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;

@Service
public class SchemaService {

    private SchemaRepository schemaRepository;
    public SchemaService(SchemaRepository schemaRepository) {
        this.schemaRepository = schemaRepository;
    }
    public boolean isSupported(URI uri) {
        // do we know the schema of the given fsm?
        return schemaRepository.existsById(uri);
    }

    public List<Schema> get() {
        return schemaRepository.findAll();
    }

    public Optional<Schema> getByUuid(UUID uuid) {
        return schemaRepository.findByUuid(uuid);
    }

    public Optional<Schema> register(SchemaDto dto) {
        // check if it was already registered
        if (schemaRepository.existsById(dto.getUri()))
            return Optional.empty();

        Schema schema = new Schema(dto.getUri(),
                dto.getName(),
                dto.getVersion(),
                dto.getSchemaJson(),
                dto.isDeprecated(),
                UUID.randomUUID());

        return Optional.of(schemaRepository.save(schema));
    }

    public boolean register(String name, String version, URI uri, String schemaJson, boolean deprecated) {
        // check if it was already registered
        if (schemaRepository.existsById(uri))
            return false;

        Schema schema = new Schema(uri, name, version, schemaJson,
                deprecated, UUID.randomUUID());

        schemaRepository.save(schema);
        return true;
    }

    public boolean deleteByUri(URI uri) {
        // check if it was already registered
        if (!schemaRepository.existsById(uri))
            return false;

        schemaRepository.deleteById(uri);
        return true;
    }

    public boolean deleteByUuid(UUID uuid) {
        // check if it was already registered
        if (!schemaRepository.existsByUuid(uuid))
            return false;

        schemaRepository.deleteByUuid(uuid);
        return true;
    }

    public boolean isValid(URI schemaUri, String json) {
        Optional<Schema> schema = schemaRepository.findById(schemaUri);
        if (!schema.isPresent())
            return false;
        return isValid(schema.get(), json);
    }

    public boolean isValid(Schema schema, String json) {
        ObjectMapper mapper = JsonMapper.builder()
                .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS).build();
        try {
            JsonNode jsonNode = mapper.readTree(json);
            JsonSchema jsonSchema;
            // TODO: schemamapper -> /schemas https://github.com/networknt/json-schema-validator
            JsonSchemaFactory factory = JsonSchemaFactory.getInstance(
                    SpecVersion.VersionFlag.V4);
            jsonSchema = factory.getSchema(schema.getSchemaJson());
            Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
            return errors.isEmpty();
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    // only for testing
    public void clear() {
        schemaRepository.deleteAll();
    }
}