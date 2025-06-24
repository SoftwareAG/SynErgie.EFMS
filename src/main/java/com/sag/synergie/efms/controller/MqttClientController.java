/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.sag.synergie.efms.controller;

import com.sag.synergie.efms.model.MqttClientConfig;
import com.sag.synergie.efms.services.MqttClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/efms/messaging/clients/mqtt")
public class MqttClientController {
    private MqttClientService mqttClientService;

    public MqttClientController(MqttClientService mqttClientService) {
        this.mqttClientService = mqttClientService;
    }

    @GetMapping
    public List<MqttClientConfig> getMqttClientConfigs() {
        List<MqttClientConfig> clientConfigs = mqttClientService.getAll();
        // TODO: drop password field entirely
        clientConfigs.parallelStream()
                .forEach(c->c.setPassword(null));
        return clientConfigs;
    }

    @GetMapping(value="/{clientId}")
    public ResponseEntity<MqttClientConfig> getMqttClientConfig(@PathVariable("clientId")String clientId) {
        Optional<MqttClientConfig> clientConfig = mqttClientService.getById(clientId);
        if (!clientConfig.isPresent())
            return ResponseEntity.notFound().build();
        // hide password
        clientConfig.get().setPassword(null);
        return ResponseEntity.ok(clientConfig.get());
    }

    @PostMapping
    public ResponseEntity<MqttClientConfig> addClientConfig(@RequestBody MqttClientConfig clientConfig) {
        // client config provided by user?
        if (null == clientConfig.getId() || clientConfig.getId().isEmpty()) {
            // assign a random UUID
            clientConfig.setId(UUID.randomUUID().toString());
        }
        else {
            // ID provided - any collisions?
            if (mqttClientService.hasConfig(clientConfig.getId()))
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Optional<MqttClientConfig> addedConfig = mqttClientService.addConfig(clientConfig);
        if (addedConfig.isPresent()) {
            // also exclude password in response
            addedConfig.get().setPassword(null);
            return ResponseEntity.ok(addedConfig.get());
        }
        else return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping(value="/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable("clientId")String clientId) {
        if (!mqttClientService.hasConfig(clientId))
            return ResponseEntity.notFound().build();
        if (mqttClientService.delete(clientId))
            return ResponseEntity.noContent().build();
        else return ResponseEntity.internalServerError().build();
    }

    @PutMapping(value="/{clientId}")
    public ResponseEntity<Void> updateClient(@PathVariable("clientId")String clientId,
                                             @RequestBody MqttClientConfig clientConfig) {
        // integrity check: path matches config value?
        if (!clientId.equals(clientConfig.getId()))
            return ResponseEntity.badRequest().build();
        if (!mqttClientService.hasConfig(clientId))
            return ResponseEntity.notFound().build();
        if (mqttClientService.update(clientConfig))
            return ResponseEntity.ok().build();
        else return ResponseEntity.internalServerError().build();
    }
}
