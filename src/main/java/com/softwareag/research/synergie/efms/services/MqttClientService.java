/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.softwareag.research.synergie.efms.services;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

import com.softwareag.research.synergie.efms.model.MqttClientConfig;
import com.softwareag.research.synergie.efms.repositories.MqttClientRepository;

import java.util.*;

@Service
public class MqttClientService {
    private MqttClientRepository mqttClientRepository;
    private Map<UUID, IMqttClient> clientMap;

    public MqttClientService(MqttClientRepository mqttClientRepository) {
        this.mqttClientRepository = mqttClientRepository;
        clientMap = new HashMap<UUID, IMqttClient>();
    }

    public Optional<MqttClientConfig> addConfig(MqttClientConfig clientConfig) {
        if (clientConfig.getId() == null) {
            // id not set by user, set to random UUID
            clientConfig.setId(UUID.randomUUID());
        }
        if (mqttClientRepository.existsById(clientConfig.getId()))
            return Optional.empty();
        return Optional.of(mqttClientRepository.save(clientConfig));
    }

    public boolean delete(UUID clientId) {
        if (!mqttClientRepository.existsById(clientId))
            return false;
        mqttClientRepository.deleteById(clientId);
        return true;
    }

    private Optional<IMqttClient> connect(UUID clientId, String url, String port,
                            String username, String password) {
        try {
            IMqttClient client = new MqttClient(url + ":" + port, clientId.toString());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            if (null != username)
                options.setUserName(username);
            if (null != password)
                options.setPassword(password.toCharArray());
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            client.connect(options);

            return Optional.of(client);
        } catch (MqttException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean publish(UUID clientId, String topic, String message, int qos) {
        if (!mqttClientRepository.existsById(clientId))
            return false;

        // are we connected?
        IMqttClient client;
        if (!clientMap.containsKey(clientId)) {
            Optional<MqttClientConfig> clientConfig = mqttClientRepository.findById(clientId);
            if (clientConfig.isEmpty()) {
                System.out.println("failed to load mqtt client config \"" + clientId + "\"");
                return false;
            }
            // not cached, connect
            Optional<IMqttClient> c = connect(clientId,
                    clientConfig.get().getUrl(),
                    clientConfig.get().getPort(),
                    clientConfig.get().getUsername(),
                    clientConfig.get().getPassword());

            if (c.isEmpty())
                return false;

            client = c.get();
            clientMap.put(clientId, client);

        } else {
            client = clientMap.get(clientId);
        }

        if (!client.isConnected()) {
            System.out.println("MQTT client is not connected");
            // TODO: attempt to reconnect?
            return false;
        }

        // client is ready, send message
        MqttMessage msg = new MqttMessage(message.getBytes());
        msg.setQos(qos);
        msg.setRetained(true);
        try {
            client.publish(topic, msg);
            return true;
        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<MqttClientConfig> getAll() {
        return mqttClientRepository.findAll();
    }

    public Optional<MqttClientConfig> getById(UUID clientId) {
        return mqttClientRepository.findById(clientId);
    }

    public boolean hasConfig(UUID clientId) {
        return mqttClientRepository.existsById(clientId);
    }

    public boolean update(MqttClientConfig mqttClientConfig) {
        if (!mqttClientRepository.existsById(mqttClientConfig.getId()))
            return false;
        mqttClientRepository.save(mqttClientConfig);
        return true;
    }
}