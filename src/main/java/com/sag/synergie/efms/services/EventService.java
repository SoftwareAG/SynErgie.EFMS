/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.sag.synergie.efms.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sag.synergie.efms.model.event.*;
import com.sag.synergie.efms.model.MqttEventListenerConfig;
import com.sag.synergie.efms.repositories.EventListenerRepository;
import com.sag.synergie.efms.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private static final String LL_MQTT = "MQTT";
    private ObjectMapper mapper;

    private EventRepository eventRepository;
    private EventListenerRepository eventListenerRepository;

    // supported listener implementation services:
    private List<String> supportedListenerTypes = List.of(
            LL_MQTT
    );
    private MqttClientService mqttClientService;

    public EventService(EventRepository eventRepository,
                        EventListenerRepository eventListenerRepository,
                        MqttClientService mqttClientService) {
        this.eventListenerRepository = eventListenerRepository;
        mapper = JsonMapper.builder()
                .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS).build();
    }

    public boolean addListener(EventListenerConfig listenerConfig) throws UnsupportedListenerTypeException {
        switch(listenerConfig.getType()) {
            case LL_MQTT:
                // deserialize settings json
                try {
                    MqttEventListenerConfig logListenerConfig = mapper.readValue(
                            listenerConfig.getSettings(), MqttEventListenerConfig.class);

                    // validate that mqtt client exists
                    if (!mqttClientService.hasConfig(logListenerConfig.getClientId()))
                        throw new SettingsException("unknown MQTT clientId \"" +
                                logListenerConfig.getClientId() + "\"");

                    // store config
                    eventListenerRepository.save(listenerConfig);
                } catch (JsonProcessingException e) {
                    throw new SettingsException(e.toString());
                }
                break;
            default:
                throw new UnsupportedListenerTypeException();
        }
        return false;
    }

    public void publish(Event event) {
        // first of all, store in repository
        Optional<EventDto> mappedEvent = map(event);
        if (mappedEvent.isPresent()) {
            eventRepository.save(mappedEvent.get());
        }

        List<EventListenerConfig> configs = eventListenerRepository.findAll();
        configs.forEach(c-> {
            // for now, LogListenerService handles all listener types, might move to separate services
            switch (c.getType()) {
                case LL_MQTT -> {
                    try {
                        MqttEventListenerConfig mqttListenerConfig = mapper.readValue(
                                c.getSettings(), MqttEventListenerConfig.class);

                        mqttClientService.publish(mqttListenerConfig.getClientId(),
                                mqttListenerConfig.getTopic(),
                                mappedEvent.get().getJson(),
                                mqttListenerConfig.getQos());
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
                // TODO: add types if required
            }
        });
    }

    public class UnsupportedListenerTypeException extends RuntimeException {}
    public class SettingsException extends RuntimeException {
        public SettingsException(String error) {
            super(error);
        }
    }

    private Optional<EventDto> map(Event event) {
        String type;
        String json;
        if (event instanceof AssetUpdatedEvent) {
            AssetUpdatedEvent e = (AssetUpdatedEvent) event;
            try {
                json = mapper.writeValueAsString(e);
                return Optional.of(new EventDto(
                                event.getId(),
                                "ModificationEvent", json));
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private Optional<Event> map(EventDto dto) {
        try {
            switch (dto.getType()) {
                case "ModificationEvent":
                    AssetUpdatedEvent event = mapper.readValue(dto.getJson(), AssetUpdatedEvent.class);
                    return Optional.of(event);
                default:
                    return Optional.empty();
            }
        } catch(JsonProcessingException ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }
}