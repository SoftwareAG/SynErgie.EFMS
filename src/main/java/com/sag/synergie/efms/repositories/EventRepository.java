/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.sag.synergie.efms.repositories;

import com.sag.synergie.efms.model.event.EventDto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface EventRepository extends MongoRepository<EventDto, UUID> {
}
