/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.sag.synergie.efms.repositories;

import com.sag.synergie.efms.model.MqttClientConfig;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MqttClientRepository extends MongoRepository<MqttClientConfig, UUID> {
}
