/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.softwareag.research.synergie.efms.repositories;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.softwareag.research.synergie.efms.model.MqttClientConfig;

public interface MqttClientRepository extends MongoRepository<MqttClientConfig, UUID> {
}
