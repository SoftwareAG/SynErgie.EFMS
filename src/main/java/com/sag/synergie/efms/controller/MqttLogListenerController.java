/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.sag.synergie.efms.controller;

import com.sag.synergie.efms.services.MqttClientService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/efms/loglisteners/mqtt")
public class MqttLogListenerController {

    private MqttClientService logListenerService;

    public MqttLogListenerController(MqttClientService logListenerService) {
        this.logListenerService = logListenerService;
    }

    /*@GetMapping
    public ResponseEntity<List<MqttLogListenerConfig>> getLogListeners() {
        return ResponseEntity.ok(logListenerService.getListeners());
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<MqttLogListenerConfig> getSchema(@PathVariable("id") String id) {
        Optional<MqttLogListenerConfig> logListener = logListenerService.getById(id);
        if (logListener.isPresent())
            return ResponseEntity.ok(logListener.get());
        else return ResponseEntity.notFound().build();
    }*/


    /*@PostMapping
    public ResponseEntity<Void> registerListener(@RequestBody MqttLogListenerConfigDto configDto,
                                                 HttpServletRequest request) {
        MqttLogListenerConfig config = logListenerService.add(configDto);
        URI uri = URI.create(request.getRequestURI() + "/" + config.getId());
        return ResponseEntity.created(uri).build();
    }*/
}