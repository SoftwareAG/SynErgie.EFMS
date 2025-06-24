/*
 * SPDX-FileCopyrightText: 2025 - 2025 Software GmbH, Darmstadt, Germany and/or its subsidiaries and/or its affiliates
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.sag.synergie.efms;

import com.sag.synergie.efms.model.SchemaDto;
import com.sag.synergie.efms.services.SchemaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JsonTests {
    @Autowired
    SchemaService schemaService;
    @Test
    public void validateSchema_Version1_0() throws Exception {

        assertFalse(schemaService.isSupported(new URI("https://git.ptw.maschinenbau.tu-darmstadt.de/eta-fabrik/public/energy_flexibility_data_model/-/blob/main/efdm_v0.4/JSON_schema/schema_flexibilitySpace_efdm_v040.json?ref_type=heads")));

        // register schema (if not present)
        schemaService.register(new SchemaDto(
                new URI("https://git.ptw.maschinenbau.tu-darmstadt.de/eta-fabrik/public/energy_flexibility_data_model/-/blob/main/efdm_v1.0/JSON_schema/schema_flexibility_space.json"),
                SCHEMA,
                "EDFM Version 1.0",
                "1.0.0",
                false
        ));
        Assertions.assertTrue(schemaService.isSupported(new URI("https://git.ptw.maschinenbau.tu-darmstadt.de/eta-fabrik/public/energy_flexibility_data_model/-/blob/main/efdm_v1.0/JSON_schema/schema_flexibility_space.json")));

        // validate valid json
        String validJson = VALID_JSON;
        //assertTrue(schemaService.isValid(validJson));

        String invalidJson = INVALID_JSON;
        //assertFalse(schemaService.isValid(invalidJson));
    }


    private static final String SCHEMA = "{  \"$schema\": \"https://json-schema.org/draft/2019-09/schema\",\n" +
            "  \"$comment\": \"This is a JSON-Schema for the flexibility space of the SynErgie EnergyFlexibilityDataModel EFDM - For further information check the repository.\",\n" +
            "  \"version\": \"1.0.0\",\n" +
            "  \"type\": \"object\",\n" +
            "  \"properties\": {\n" +
            "    \"metadata\": {\n" +
            "      \"$ref\": \"https://git.ptw.maschinenbau.tu-darmstadt.de/eta-fabrik/public/energy_flexibility_data_model/-/raw/main/efdm_v1.0/JSON_schema/schema_metadata.json?ref_type=heads\"\n" +
            "    },\n" +
            "    \"flexibleLoads\": {\n" +
            "      \"type\": \"array\",\n" +
            "      \"minItems\": 1,\n" +
            "      \"items\": {\n" +
            "        \"type\": \"object\",\n" +
            "        \"properties\": {\n" +
            "          \"flexibleLoadId\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"uuid\": {\n" +
            "                \"type\": \"string\",\n" +
            "                \"format\": \"uuid\"\n" +
            "              },\n" +
            "              \"comment\": {\n" +
            "                \"type\": \"string\"\n" +
            "              }\n" +
            "            },\n" +
            "            \"required\": [\n" +
            "              \"uuid\"\n" +
            "            ],\n" +
            "            \"additionalProperties\": false\n" +
            "          },\n" +
            "          \"reactionDuration\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"unit\": {\n" +
            "                \"type\": \"string\",\n" +
            "                \"const\": \"s\"\n" +
            "              },\n" +
            "              \"value\": {\n" +
            "                \"type\": \"number\",\n" +
            "                \"minimum\": 0,\n" +
            "                \"multipleOf\": 0.001\n" +
            "              }\n" +
            "            },\n" +
            "            \"required\": [\n" +
            "              \"unit\",\n" +
            "              \"value\"\n" +
            "            ],\n" +
            "            \"additionalProperties\": false\n" +
            "          },\n" +
            "          \"validity\": {\n" +
            "            \"type\": \"array\",\n" +
            "            \"minItems\": 1,\n" +
            "            \"items\": {\n" +
            "              \"type\": \"object\",\n" +
            "              \"properties\": {\n" +
            "                \"from\": {\n" +
            "                  \"type\": \"string\",\n" +
            "                  \"format\": \"date-time\"\n" +
            "                },\n" +
            "                \"until\": {\n" +
            "                  \"type\": \"string\",\n" +
            "                  \"format\": \"date-time\"\n" +
            "                },\n" +
            "                \"temporalType\": {\n" +
            "                  \"type\": \"string\",\n" +
            "                  \"enum\": [\n" +
            "                    \"start\",\n" +
            "                    \"total\",\n" +
            "                    \"end\"\n" +
            "                  ]\n" +
            "                }\n" +
            "              },\n" +
            "              \"required\": [\n" +
            "                \"from\",\n" +
            "                \"until\",\n" +
            "                \"temporalType\"\n" +
            "              ],\n" +
            "              \"additionalProperties\": false\n" +
            "            }\n" +
            "          },\n" +
            "          \"powerStates\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"order\": {\n" +
            "                \"type\": \"string\",\n" +
            "                \"enum\": [\n" +
            "                  \"chronological\",\n" +
            "                  \"arbitrary\"\n" +
            "                ]\n" +
            "              },\n" +
            "              \"durationType\": {\n" +
            "                \"type\": \"string\",\n" +
            "                \"enum\": [\n" +
            "                  \"deliveryDuration\",\n" +
            "                  \"holdingDuration\"\n" +
            "                ]\n" +
            "              },\n" +
            "              \"values\": {\n" +
            "                \"type\": \"array\",\n" +
            "                \"minItems\": 1,\n" +
            "                \"items\": {\n" +
            "                  \"type\": \"object\",\n" +
            "                  \"properties\": {\n" +
            "                    \"power\": {\n" +
            "                      \"type\": \"object\",\n" +
            "                      \"properties\": {\n" +
            "                        \"unit\": {\n" +
            "                          \"type\": \"string\",\n" +
            "                          \"const\": \"kW\"\n" +
            "                        },\n" +
            "                        \"minValue\": {\n" +
            "                          \"type\": \"number\",\n" +
            "                          \"multipleOf\": 0.001\n" +
            "                        },\n" +
            "                        \"maxValue\": {\n" +
            "                          \"type\": \"number\",\n" +
            "                          \"multipleOf\": 0.001\n" +
            "                        }\n" +
            "                      },\n" +
            "                      \"required\": [\n" +
            "                        \"unit\",\n" +
            "                        \"minValue\",\n" +
            "                        \"maxValue\"\n" +
            "                      ],\n" +
            "                      \"additionalProperties\": false\n" +
            "                    },\n" +
            "                    \"duration\": {\n" +
            "                      \"type\": \"object\",\n" +
            "                      \"properties\": {\n" +
            "                        \"unit\": {\n" +
            "                          \"type\": \"string\",\n" +
            "                          \"const\": \"s\"\n" +
            "                        },\n" +
            "                        \"minValue\": {\n" +
            "                          \"type\": \"number\",\n" +
            "                          \"minimum\": 0,\n" +
            "                          \"exclusiveMinimum\": 0,\n" +
            "                          \"multipleOf\": 0.001\n" +
            "                        },\n" +
            "                        \"maxValue\": {\n" +
            "                          \"type\": \"number\",\n" +
            "                          \"minimum\": 0,\n" +
            "                          \"exclusiveMinimum\": 0,\n" +
            "                          \"multipleOf\": 0.001\n" +
            "                        }\n" +
            "                      },\n" +
            "                      \"required\": [\n" +
            "                        \"unit\"\n" +
            "                      ],\n" +
            "                      \"additionalProperties\": false\n" +
            "                    }\n" +
            "                  },\n" +
            "                  \"required\": [\n" +
            "                    \"power\"\n" +
            "                  ],\n" +
            "                  \"additionalProperties\": false\n" +
            "                }\n" +
            "              }\n" +
            "            },\n" +
            "            \"required\": [\n" +
            "              \"values\"\n" +
            "            ],\n" +
            "            \"additionalProperties\": false\n" +
            "          },\n" +
            "          \"usageNumber\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"unit\": {\n" +
            "                \"type\": \"string\",\n" +
            "                \"const\": \"None\"\n" +
            "              },\n" +
            "              \"minValue\": {\n" +
            "                \"type\": \"number\",\n" +
            "                \"multipleOf\": 1,\n" +
            "                \"minimum\": 0\n" +
            "              },\n" +
            "              \"maxValue\": {\n" +
            "                \"type\": \"number\",\n" +
            "                \"multipleOf\": 1,\n" +
            "                \"minimum\": 0\n" +
            "              }\n" +
            "            },\n" +
            "            \"required\": [\n" +
            "              \"unit\"\n" +
            "            ],\n" +
            "            \"additionalProperties\": false\n" +
            "          },\n" +
            "          \"modulationNumber\": {\n" +
            "            \"type\": \"number\",\n" +
            "            \"multipleOf\": 1,\n" +
            "            \"minimum\": 0\n" +
            "          },\n" +
            "          \"powerGradients\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"activationGradient\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                  \"unit\": {\n" +
            "                    \"type\": \"string\",\n" +
            "                    \"const\": \"kW/s\"\n" +
            "                  },\n" +
            "                  \"minValue\": {\n" +
            "                    \"type\": \"number\",\n" +
            "                    \"minimum\": 0,\n" +
            "                    \"multipleOf\": 0.001\n" +
            "                  },\n" +
            "                  \"maxValue\": {\n" +
            "                    \"type\": \"number\",\n" +
            "                    \"minimum\": 0,\n" +
            "                    \"multipleOf\": 0.001\n" +
            "                  }\n" +
            "                },\n" +
            "                \"required\": [\n" +
            "                  \"unit\"\n" +
            "                ],\n" +
            "                \"additionalProperties\": false\n" +
            "              },\n" +
            "              \"modulationGradient\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                  \"unit\": {\n" +
            "                    \"type\": \"string\",\n" +
            "                    \"const\": \"kW/s\"\n" +
            "                  },\n" +
            "                  \"minValue\": {\n" +
            "                    \"type\": \"number\",\n" +
            "                    \"minimum\": 0,\n" +
            "                    \"multipleOf\": 0.001\n" +
            "                  },\n" +
            "                  \"maxValue\": {\n" +
            "                    \"type\": \"number\",\n" +
            "                    \"minimum\": 0,\n" +
            "                    \"multipleOf\": 0.001\n" +
            "                  }\n" +
            "                },\n" +
            "                \"required\": [\n" +
            "                  \"unit\"\n" +
            "                ],\n" +
            "                \"additionalProperties\": false\n" +
            "              },\n" +
            "              \"deactivationGradient\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                  \"unit\": {\n" +
            "                    \"type\": \"string\",\n" +
            "                    \"const\": \"kW/s\"\n" +
            "                  },\n" +
            "                  \"minValue\": {\n" +
            "                    \"type\": \"number\",\n" +
            "                    \"minimum\": 0,\n" +
            "                    \"multipleOf\": 0.001\n" +
            "                  },\n" +
            "                  \"maxValue\": {\n" +
            "                    \"type\": \"number\",\n" +
            "                    \"minimum\": 0,\n" +
            "                    \"multipleOf\": 0.001\n" +
            "                  }\n" +
            "                },\n" +
            "                \"required\": [\n" +
            "                  \"unit\"\n" +
            "                ],\n" +
            "                \"additionalProperties\": false\n" +
            "              }\n" +
            "            },\n" +
            "            \"additionalProperties\": false\n" +
            "          },\n" +
            "          \"regenerationDuration\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"unit\": {\n" +
            "                \"type\": \"string\",\n" +
            "                \"const\": \"s\"\n" +
            "              },\n" +
            "              \"value\": {\n" +
            "                \"type\": \"number\",\n" +
            "                \"minimum\": 0,\n" +
            "                \"multipleOf\": 0.001\n" +
            "              }\n" +
            "            },\n" +
            "            \"required\": [\n" +
            "              \"unit\",\n" +
            "              \"value\"\n" +
            "            ],\n" +
            "            \"additionalProperties\": false\n" +
            "          },\n" +
            "          \"flexibleLoadCosts\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"variableCost\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                  \"unit\": {\n" +
            "                    \"type\": \"string\",\n" +
            "                    \"const\": \"EUR/kWh\"\n" +
            "                  },\n" +
            "                  \"value\": {\n" +
            "                    \"type\": \"number\",\n" +
            "                    \"multipleOf\": 0.001\n" +
            "                  }\n" +
            "                },\n" +
            "                \"required\": [\n" +
            "                  \"unit\",\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"additionalProperties\": false\n" +
            "              },\n" +
            "              \"costPerUsage\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                  \"unit\": {\n" +
            "                    \"type\": \"string\",\n" +
            "                    \"const\": \"EUR/usage\"\n" +
            "                  },\n" +
            "                  \"value\": {\n" +
            "                    \"type\": \"number\",\n" +
            "                    \"multipleOf\": 0.001\n" +
            "                  }\n" +
            "                },\n" +
            "                \"required\": [\n" +
            "                  \"unit\",\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"additionalProperties\": false\n" +
            "              },\n" +
            "              \"fixedCost\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                  \"unit\": {\n" +
            "                    \"type\": \"string\",\n" +
            "                    \"const\": \"EUR\"\n" +
            "                  },\n" +
            "                  \"value\": {\n" +
            "                    \"type\": \"number\",\n" +
            "                    \"multipleOf\": 0.001\n" +
            "                  }\n" +
            "                },\n" +
            "                \"required\": [\n" +
            "                  \"unit\",\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"additionalProperties\": false\n" +
            "              }\n" +
            "            },\n" +
            "            \"additionalProperties\": false\n" +
            "          },\n" +
            "          \"orderConfirmationDeadline\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"deadlineType\": {\n" +
            "                \"type\": \"string\",\n" +
            "                \"enum\": [\n" +
            "                  \"relative\",\n" +
            "                  \"absolute\"\n" +
            "                ]\n" +
            "              }\n" +
            "            },\n" +
            "            \"allOf\": [\n" +
            "              {\n" +
            "                \"if\": {\n" +
            "                  \"properties\": {\n" +
            "                    \"deadlineType\": {\n" +
            "                      \"const\": \"absolute\"\n" +
            "                    }\n" +
            "                  }\n" +
            "                },\n" +
            "                \"then\": {\n" +
            "                  \"properties\": {\n" +
            "                    \"deadlineValue\": {\n" +
            "                      \"type\": \"string\",\n" +
            "                      \"format\": \"date-time\"\n" +
            "                    }\n" +
            "                  },\n" +
            "                  \"required\": [\n" +
            "                    \"deadlineValue\"\n" +
            "                  ]\n" +
            "                }\n" +
            "              },\n" +
            "              {\n" +
            "                \"if\": {\n" +
            "                  \"properties\": {\n" +
            "                    \"deadlineType\": {\n" +
            "                      \"const\": \"relative\"\n" +
            "                    }\n" +
            "                  }\n" +
            "                },\n" +
            "                \"then\": {\n" +
            "                  \"properties\": {\n" +
            "                    \"deadlineValue\": {\n" +
            "                      \"type\": \"object\",\n" +
            "                      \"properties\": {\n" +
            "                        \"unit\": {\n" +
            "                          \"type\": \"string\",\n" +
            "                          \"const\": \"s\"\n" +
            "                        },\n" +
            "                        \"value\": {\n" +
            "                          \"type\": \"number\",\n" +
            "                          \"minimum\": 0,\n" +
            "                          \"multipleOf\": 0.001\n" +
            "                        }\n" +
            "                      },\n" +
            "                      \"required\": [\n" +
            "                        \"unit\",\n" +
            "                        \"value\"\n" +
            "                      ]\n" +
            "                    }\n" +
            "                  },\n" +
            "                  \"required\": [\n" +
            "                    \"deadlineValue\"\n" +
            "                  ]\n" +
            "                }\n" +
            "              }\n" +
            "            ],\n" +
            "            \"unevaluatedProperties\": false,\n" +
            "            \"required\": [\n" +
            "              \"deadlineType\",\n" +
            "              \"deadlineValue\"\n" +
            "            ]\n" +
            "          },\n" +
            "          \"prices\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"variablePrice\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                  \"unit\": {\n" +
            "                    \"type\": \"string\",\n" +
            "                    \"const\": \"EUR/kWh\"\n" +
            "                  },\n" +
            "                  \"value\": {\n" +
            "                    \"type\": \"number\",\n" +
            "                    \"multipleOf\": 0.001\n" +
            "                  }\n" +
            "                },\n" +
            "                \"required\": [\n" +
            "                  \"unit\",\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"additionalProperties\": false\n" +
            "              },\n" +
            "              \"pricePerUsage\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                  \"unit\": {\n" +
            "                    \"type\": \"string\",\n" +
            "                    \"const\": \"EUR/usage\"\n" +
            "                  },\n" +
            "                  \"value\": {\n" +
            "                    \"type\": \"number\",\n" +
            "                    \"multipleOf\": 0.001\n" +
            "                  }\n" +
            "                },\n" +
            "                \"required\": [\n" +
            "                  \"unit\",\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"additionalProperties\": false\n" +
            "              },\n" +
            "              \"fixedPrice\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                  \"unit\": {\n" +
            "                    \"type\": \"string\",\n" +
            "                    \"const\": \"EUR\"\n" +
            "                  },\n" +
            "                  \"value\": {\n" +
            "                    \"type\": \"number\",\n" +
            "                    \"multipleOf\": 0.001\n" +
            "                  }\n" +
            "                },\n" +
            "                \"required\": [\n" +
            "                  \"unit\",\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"additionalProperties\": false\n" +
            "              }\n" +
            "            },\n" +
            "            \"additionalProperties\": false\n" +
            "          },\n" +
            "          \"location\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"meterLocation\": {\n" +
            "                \"type\": \"string\"\n" +
            "              },\n" +
            "              \"voltageLevel\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                  \"unit\": {\n" +
            "                    \"type\": \"string\",\n" +
            "                    \"const\": \"kV\"\n" +
            "                  },\n" +
            "                  \"value\": {\n" +
            "                    \"type\": \"number\",\n" +
            "                    \"minimum\": 0,\n" +
            "                    \"exclusiveMinimum\": 0,\n" +
            "                    \"multipleOf\": 0.001\n" +
            "                  }\n" +
            "                },\n" +
            "                \"required\": [\n" +
            "                  \"unit\",\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"additionalProperties\": false\n" +
            "              }\n" +
            "            },\n" +
            "            \"required\": [\n" +
            "              \"meterLocation\",\n" +
            "              \"voltageLevel\"\n" +
            "            ],\n" +
            "            \"additionalProperties\": false\n" +
            "          }\n" +
            "        },\n" +
            "        \"required\": [\n" +
            "          \"flexibleLoadId\",\n" +
            "          \"powerStates\"\n" +
            "        ],\n" +
            "        \"additionalProperties\": false\n" +
            "      }\n" +
            "    },\n" +
            "    \"storages\": {\n" +
            "      \"type\": \"array\",\n" +
            "      \"minItems\": 1,\n" +
            "      \"items\": {\n" +
            "        \"type\": \"object\",\n" +
            "        \"properties\": {\n" +
            "          \"storageId\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"uuid\": {\n" +
            "                \"type\": \"string\",\n" +
            "                \"format\": \"uuid\"\n" +
            "              },\n" +
            "              \"comment\": {\n" +
            "                \"type\": \"string\"\n" +
            "              }\n" +
            "            },\n" +
            "            \"required\": [\n" +
            "              \"uuid\"\n" +
            "            ],\n" +
            "            \"additionalProperties\": false\n" +
            "          },\n" +
            "          \"usableCapacity\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"unit\": {\n" +
            "                \"type\": \"string\",\n" +
            "                \"const\": \"kWh\"\n" +
            "              },\n" +
            "              \"minValue\": {\n" +
            "                \"type\": \"number\",\n" +
            "                \"multipleOf\": 0.001\n" +
            "              },\n" +
            "              \"maxValue\": {\n" +
            "                \"type\": \"number\",\n" +
            "                \"multipleOf\": 0.001\n" +
            "              }\n" +
            "            },\n" +
            "            \"required\": [\n" +
            "              \"unit\",\n" +
            "              \"minValue\",\n" +
            "              \"maxValue\"\n" +
            "            ],\n" +
            "            \"additionalProperties\": false\n" +
            "          },\n" +
            "          \"initialEnergyContent\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"unit\": {\n" +
            "                \"type\": \"string\",\n" +
            "                \"const\": \"kWh\"\n" +
            "              },\n" +
            "              \"minValue\": {\n" +
            "                \"type\": \"number\",\n" +
            "                \"multipleOf\": 0.001\n" +
            "              },\n" +
            "              \"maxValue\": {\n" +
            "                \"type\": \"number\",\n" +
            "                \"multipleOf\": 0.001\n" +
            "              }\n" +
            "            },\n" +
            "            \"required\": [\n" +
            "              \"unit\",\n" +
            "              \"maxValue\",\n" +
            "              \"minValue\"\n" +
            "            ],\n" +
            "            \"additionalProperties\": false\n" +
            "          },\n" +
            "          \"targetEnergyContent\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"unit\": {\n" +
            "                \"type\": \"string\",\n" +
            "                \"const\": \"kWh\"\n" +
            "              },\n" +
            "              \"minValue\": {\n" +
            "                \"type\": \"number\",\n" +
            "                \"multipleOf\": 0.001\n" +
            "              },\n" +
            "              \"maxValue\": {\n" +
            "                \"type\": \"number\",\n" +
            "                \"multipleOf\": 0.001\n" +
            "              }\n" +
            "            },\n" +
            "            \"required\": [\n" +
            "              \"unit\"\n" +
            "            ],\n" +
            "            \"additionalProperties\": false\n" +
            "          },\n" +
            "          \"energyLoss\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"unit\": {\n" +
            "                \"type\": \"string\",\n" +
            "                \"const\": \"%/h\"\n" +
            "              },\n" +
            "              \"value\": {\n" +
            "                \"type\": \"number\",\n" +
            "                \"minimum\": 0,\n" +
            "                \"maximum\": 100,\n" +
            "                \"multipleOf\": 0.001\n" +
            "              }\n" +
            "            },\n" +
            "            \"required\": [\n" +
            "              \"unit\",\n" +
            "              \"value\"\n" +
            "            ],\n" +
            "            \"additionalProperties\": false\n" +
            "          },\n" +
            "          \"suppliers\": {\n" +
            "            \"type\": \"array\",\n" +
            "            \"minItems\": 1,\n" +
            "            \"items\": {\n" +
            "              \"type\": \"object\",\n" +
            "              \"properties\": {\n" +
            "                \"flexibleLoadId\": {\n" +
            "                  \"type\": \"object\",\n" +
            "                  \"properties\": {\n" +
            "                    \"uuid\": {\n" +
            "                      \"type\": \"string\",\n" +
            "                      \"format\": \"uuid\"\n" +
            "                    },\n" +
            "                    \"comment\": {\n" +
            "                      \"type\": \"string\"\n" +
            "                    }\n" +
            "                  },\n" +
            "                  \"required\": [\n" +
            "                    \"uuid\"\n" +
            "                  ],\n" +
            "                  \"additionalProperties\": false\n" +
            "                },\n" +
            "                \"conversionEfficiency\": {\n" +
            "                  \"type\": \"object\",\n" +
            "                  \"properties\": {\n" +
            "                    \"unit\": {\n" +
            "                      \"type\": \"string\",\n" +
            "                      \"const\": \"%\"\n" +
            "                    },\n" +
            "                    \"value\": {\n" +
            "                      \"type\": \"number\",\n" +
            "                      \"minimum\": 0,\n" +
            "                      \"exclusiveMinimum\": 0,\n" +
            "                      \"multipleOf\": 0.001\n" +
            "                    }\n" +
            "                  },\n" +
            "                  \"required\": [\n" +
            "                    \"unit\",\n" +
            "                    \"value\"\n" +
            "                  ],\n" +
            "                  \"additionalProperties\": false\n" +
            "                }\n" +
            "              },\n" +
            "              \"required\": [\n" +
            "                \"flexibleLoadId\"\n" +
            "              ],\n" +
            "              \"additionalProperties\": false\n" +
            "            }\n" +
            "          },\n" +
            "          \"drain\": {\n" +
            "            \"type\": \"array\",\n" +
            "            \"minItems\": 1,\n" +
            "            \"items\": {\n" +
            "              \"type\": \"object\",\n" +
            "              \"properties\": {\n" +
            "                \"power\": {\n" +
            "                  \"type\": \"object\",\n" +
            "                  \"properties\": {\n" +
            "                    \"unit\": {\n" +
            "                      \"type\": \"string\",\n" +
            "                      \"const\": \"kW\"\n" +
            "                    },\n" +
            "                    \"value\": {\n" +
            "                      \"type\": \"number\",\n" +
            "                      \"multipleOf\": 0.001\n" +
            "                    }\n" +
            "                  },\n" +
            "                  \"required\": [\n" +
            "                    \"unit\",\n" +
            "                    \"value\"\n" +
            "                  ],\n" +
            "                  \"additionalProperties\": false\n" +
            "                },\n" +
            "                \"timestamp\": {\n" +
            "                  \"type\": \"string\",\n" +
            "                  \"format\": \"date-time\"\n" +
            "                }\n" +
            "              },\n" +
            "              \"required\": [\n" +
            "                \"power\",\n" +
            "                \"timestamp\"\n" +
            "              ],\n" +
            "              \"additionalProperties\": false\n" +
            "            }\n" +
            "          },\n" +
            "          \"storageCosts\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"variableCost\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                  \"unit\": {\n" +
            "                    \"type\": \"string\",\n" +
            "                    \"const\": \"EUR/kWh\"\n" +
            "                  },\n" +
            "                  \"value\": {\n" +
            "                    \"type\": \"number\",\n" +
            "                    \"multipleOf\": 0.001\n" +
            "                  }\n" +
            "                },\n" +
            "                \"required\": [\n" +
            "                  \"unit\",\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"additionalProperties\": false\n" +
            "              },\n" +
            "              \"costPerUsage\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                  \"unit\": {\n" +
            "                    \"type\": \"string\",\n" +
            "                    \"const\": \"EUR/usage\"\n" +
            "                  },\n" +
            "                  \"value\": {\n" +
            "                    \"type\": \"number\",\n" +
            "                    \"multipleOf\": 0.001\n" +
            "                  }\n" +
            "                },\n" +
            "                \"required\": [\n" +
            "                  \"unit\",\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"additionalProperties\": false\n" +
            "              },\n" +
            "              \"fixedCost\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                  \"unit\": {\n" +
            "                    \"type\": \"string\",\n" +
            "                    \"const\": \"EUR\"\n" +
            "                  },\n" +
            "                  \"value\": {\n" +
            "                    \"type\": \"number\",\n" +
            "                    \"multipleOf\": 0.001\n" +
            "                  }\n" +
            "                },\n" +
            "                \"required\": [\n" +
            "                  \"unit\",\n" +
            "                  \"value\"\n" +
            "                ],\n" +
            "                \"additionalProperties\": false\n" +
            "              }\n" +
            "            },\n" +
            "            \"additionalProperties\": false\n" +
            "          }\n" +
            "        },\n" +
            "        \"required\": [\n" +
            "          \"storageId\",\n" +
            "          \"usableCapacity\",\n" +
            "          \"initialEnergyContent\",\n" +
            "          \"suppliers\"\n" +
            "        ],\n" +
            "        \"additionalProperties\": false\n" +
            "      }\n" +
            "    },\n" +
            "    \"dependencies\": {\n" +
            "      \"type\": \"array\",\n" +
            "      \"minItems\": 1,\n" +
            "      \"items\": {\n" +
            "        \"type\": \"object\",\n" +
            "        \"properties\": {\n" +
            "          \"dependencyId\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"uuid\": {\n" +
            "                \"type\": \"string\",\n" +
            "                \"format\": \"uuid\"\n" +
            "              },\n" +
            "              \"comment\": {\n" +
            "                \"type\": \"string\"\n" +
            "              }\n" +
            "            },\n" +
            "            \"required\": [\n" +
            "              \"uuid\"\n" +
            "            ],\n" +
            "            \"additionalProperties\": false\n" +
            "          },\n" +
            "          \"triggeringFlexibleLoad\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"temporalType\": {\n" +
            "                \"type\": \"string\",\n" +
            "                \"enum\": [\n" +
            "                  \"start\",\n" +
            "                  \"total\",\n" +
            "                  \"end\"\n" +
            "                ]\n" +
            "              },\n" +
            "              \"triggeringFlexibleLoadId\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                  \"uuid\": {\n" +
            "                    \"type\": \"string\",\n" +
            "                    \"format\": \"uuid\"\n" +
            "                  },\n" +
            "                  \"comment\": {\n" +
            "                    \"type\": \"string\"\n" +
            "                  }\n" +
            "                },\n" +
            "                \"required\": [\n" +
            "                  \"uuid\"\n" +
            "                ],\n" +
            "                \"additionalProperties\": false\n" +
            "              }\n" +
            "            },\n" +
            "            \"required\": [\n" +
            "              \"temporalType\",\n" +
            "              \"triggeringFlexibleLoadId\"\n" +
            "            ],\n" +
            "            \"additionalProperties\": false\n" +
            "          },\n" +
            "          \"targetFlexibleLoad\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"temporalType\": {\n" +
            "                \"type\": \"string\",\n" +
            "                \"enum\": [\n" +
            "                  \"start\",\n" +
            "                  \"total\",\n" +
            "                  \"end\"\n" +
            "                ]\n" +
            "              },\n" +
            "              \"targetFlexibleLoadId\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                  \"uuid\": {\n" +
            "                    \"type\": \"string\",\n" +
            "                    \"format\": \"uuid\"\n" +
            "                  },\n" +
            "                  \"comment\": {\n" +
            "                    \"type\": \"string\"\n" +
            "                  }\n" +
            "                },\n" +
            "                \"required\": [\n" +
            "                  \"uuid\"\n" +
            "                ],\n" +
            "                \"additionalProperties\": false\n" +
            "              }\n" +
            "            },\n" +
            "            \"required\": [\n" +
            "              \"temporalType\",\n" +
            "              \"targetFlexibleLoadId\"\n" +
            "            ],\n" +
            "            \"additionalProperties\": false\n" +
            "          },\n" +
            "          \"logicalType\": {\n" +
            "            \"type\": \"string\",\n" +
            "            \"enum\": [\n" +
            "              \"implies\",\n" +
            "              \"excludes\"\n" +
            "            ]\n" +
            "          },\n" +
            "          \"applicabilityDuration\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"properties\": {\n" +
            "              \"unit\": {\n" +
            "                \"type\": \"string\",\n" +
            "                \"const\": \"s\"\n" +
            "              },\n" +
            "              \"minValue\": {\n" +
            "                \"type\": \"number\",\n" +
            "                \"multipleOf\": 0.001\n" +
            "              },\n" +
            "              \"maxValue\": {\n" +
            "                \"type\": \"number\",\n" +
            "                \"multipleOf\": 0.001\n" +
            "              }\n" +
            "            },\n" +
            "            \"required\": [\n" +
            "              \"unit\"\n" +
            "            ],\n" +
            "            \"additionalProperties\": false\n" +
            "          },\n" +
            "          \"applicabilityConditions\": {\n" +
            "            \"type\": \"array\",\n" +
            "            \"minItems\": 1,\n" +
            "            \"items\": {\n" +
            "              \"type\": \"object\",\n" +
            "              \"properties\": {\n" +
            "                \"formulaLeft\": {\n" +
            "                  \"type\": \"string\"\n" +
            "                },\n" +
            "                \"formulaRight\": {\n" +
            "                  \"type\": \"string\"\n" +
            "                },\n" +
            "                \"comparator\": {\n" +
            "                  \"type\": \"string\",\n" +
            "                  \"enum\": [\n" +
            "                    \"equals\",\n" +
            "                    \"less\",\n" +
            "                    \"lessEqual\",\n" +
            "                    \"greater\",\n" +
            "                    \"greaterEqual\"\n" +
            "                  ]\n" +
            "                }\n" +
            "              },\n" +
            "              \"required\": [\n" +
            "                \"formulaLeft\",\n" +
            "                \"formulaRight\",\n" +
            "                \"comparator\"\n" +
            "              ],\n" +
            "              \"additionalProperties\": false\n" +
            "            }\n" +
            "          }\n" +
            "        },\n" +
            "        \"required\": [\n" +
            "          \"dependencyId\",\n" +
            "          \"triggeringFlexibleLoad\",\n" +
            "          \"targetFlexibleLoad\",\n" +
            "          \"logicalType\"\n" +
            "        ],\n" +
            "        \"additionalProperties\": false\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"required\": [\n" +
            "    \"metadata\",\n" +
            "    \"flexibleLoads\"\n" +
            "  ]\n" +
            "}";

    private static final String VALID_JSON = "{\n" +
            "  \"metadata\": {\n" +
            "    \"instanceId\": \"6e8bc430-9c3a-11d9-9669-0800200c9a66\",\n" +
            "    \"efdmVersion\": {\n" +
            "      \"versionNumber\": \"1.0.0\",\n" +
            "      \"schemaLink\": \"https://git.ptw.maschinenbau.tu-darmstadt.de/eta-fabrik/public/energy_flexibility_data_model/-/blob/main/efdm_v1.0/JSON_schema/schema_flexibility_space.json\"\n" +
            "    },\n" +
            "    \"origin\": {\n" +
            "      \"originId\": \"6e8bc430-9c3a-11d9-9669-0800200c9a67\",\n" +
            "      \"timestamp\": \"2023-10-26T14:30:15Z\"\n" +
            "    },\n" +
            "    \"modification\": {\n" +
            "      \"modifierId\": \"6e8bc430-9c3a-11d9-9669-0800200c9a68\",\n" +
            "      \"timestamp\": \"2023-10-26T15:30:15Z\"\n" +
            "    },\n" +
            "    \"comment\": \"This is a sample comment\"\n" +
            "  },\n" +
            "    \"flexibleLoads\": [\n" +
            "      {\n" +
            "        \"flexibleLoadId\": {\n" +
            "          \"uuid\": \"6e8bc430-9c3a-11d9-9669-0800200c9a69\",\n" +
            "          \"comment\": \"Flexible load UUID\"\n" +
            "        },\n" +
            "        \"reactionDuration\": {\n" +
            "          \"unit\": \"s\",\n" +
            "          \"value\": 5.000\n" +
            "        },\n" +
            "        \"validity\": [\n" +
            "          {\n" +
            "            \"from\": \"2023-10-26T14:00:00Z\",\n" +
            "            \"until\": \"2023-10-26T15:00:00Z\",\n" +
            "            \"temporalType\": \"start\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"powerStates\": {\n" +
            "          \"order\": \"chronological\",\n" +
            "          \"durationType\": \"deliveryDuration\",\n" +
            "          \"values\": [\n" +
            "            {\n" +
            "              \"power\": {\n" +
            "                \"unit\": \"kW\",\n" +
            "                \"minValue\": 1.000,\n" +
            "                \"maxValue\": 5.000\n" +
            "              },\n" +
            "              \"duration\": {\n" +
            "                \"unit\": \"s\",\n" +
            "                \"minValue\": 1.001,\n" +
            "                \"maxValue\": 10.000\n" +
            "              }\n" +
            "            }\n" +
            "          ]\n" +
            "        },\n" +
            "        \"usageNumber\": {\n" +
            "          \"unit\": \"None\",\n" +
            "          \"minValue\": 1,\n" +
            "          \"maxValue\": 5\n" +
            "        },\n" +
            "        \"modulationNumber\": 1,\n" +
            "        \"powerGradients\": {\n" +
            "          \"activationGradient\": {\n" +
            "            \"unit\": \"kW/s\",\n" +
            "            \"minValue\": 0.001,\n" +
            "            \"maxValue\": 1.000\n" +
            "          },\n" +
            "          \"modulationGradient\": {\n" +
            "            \"unit\": \"kW/s\",\n" +
            "            \"minValue\": 0.001,\n" +
            "            \"maxValue\": 1.000\n" +
            "          },\n" +
            "          \"deactivationGradient\": {\n" +
            "            \"unit\": \"kW/s\",\n" +
            "            \"minValue\": 0.001,\n" +
            "            \"maxValue\": 1.000\n" +
            "          }\n" +
            "        },\n" +
            "        \"regenerationDuration\": {\n" +
            "          \"unit\": \"s\",\n" +
            "          \"value\": 5.000\n" +
            "        },\n" +
            "        \"flexibleLoadCosts\": {\n" +
            "          \"variableCost\": {\n" +
            "            \"unit\": \"EUR/kWh\",\n" +
            "            \"value\": 0.010\n" +
            "          },\n" +
            "          \"costPerUsage\": {\n" +
            "            \"unit\": \"EUR/usage\",\n" +
            "            \"value\": 0.010\n" +
            "          },\n" +
            "          \"fixedCost\": {\n" +
            "            \"unit\": \"EUR\",\n" +
            "            \"value\": 10.000\n" +
            "          }\n" +
            "        },\n" +
            "        \"orderConfirmationDeadline\": {\n" +
            "          \"deadlineType\": \"relative\",\n" +
            "          \"deadlineValue\": {\n" +
            "            \"unit\": \"s\",\n" +
            "            \"value\": 60.000\n" +
            "          }\n" +
            "        },\n" +
            "        \"prices\": {\n" +
            "          \"variablePrice\": {\n" +
            "            \"unit\": \"EUR/kWh\",\n" +
            "            \"value\": 0.010\n" +
            "          },\n" +
            "          \"pricePerUsage\": {\n" +
            "            \"unit\": \"EUR/usage\",\n" +
            "            \"value\": 0.010\n" +
            "          },\n" +
            "          \"fixedPrice\": {\n" +
            "            \"unit\": \"EUR\",\n" +
            "            \"value\": 10.000\n" +
            "          }\n" +
            "        },\n" +
            "        \"location\": {\n" +
            "          \"meterLocation\": \"Location A\",\n" +
            "          \"voltageLevel\": {\n" +
            "            \"unit\": \"kV\",\n" +
            "            \"value\": 11.000\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    ],\n" +
            "    \"storages\": [\n" +
            "      {\n" +
            "        \"storageId\": {\n" +
            "          \"uuid\": \"6e8bc430-9c3a-11d9-9669-0800200c9a66\",\n" +
            "          \"comment\": \"Storage UUID\"\n" +
            "        },\n" +
            "        \"usableCapacity\": {\n" +
            "          \"unit\": \"kWh\",\n" +
            "          \"minValue\": 1.000,\n" +
            "          \"maxValue\": 10.000\n" +
            "        },\n" +
            "        \"initialEnergyContent\": {\n" +
            "          \"unit\": \"kWh\",\n" +
            "          \"minValue\": 2.000,\n" +
            "          \"maxValue\": 8.000\n" +
            "        },\n" +
            "        \"targetEnergyContent\": {\n" +
            "          \"unit\": \"kWh\",\n" +
            "          \"minValue\": 5.000,\n" +
            "          \"maxValue\": 9.000\n" +
            "        },\n" +
            "        \"energyLoss\": {\n" +
            "          \"unit\": \"%/h\",\n" +
            "          \"value\": 0.500\n" +
            "        },\n" +
            "        \"suppliers\": [\n" +
            "          {\n" +
            "            \"flexibleLoadId\": {\n" +
            "              \"uuid\": \"6e8bc430-9c3a-11d9-9669-0800200c9a69\",\n" +
            "              \"comment\": \"Supplier UUID\"\n" +
            "            },\n" +
            "            \"conversionEfficiency\": {\n" +
            "              \"unit\": \"%\",\n" +
            "              \"value\": 98.000\n" +
            "            }\n" +
            "          }\n" +
            "        ],\n" +
            "        \"drain\": [\n" +
            "          {\n" +
            "            \"power\": {\n" +
            "              \"unit\": \"kW\",\n" +
            "              \"value\": 5.000\n" +
            "            },\n" +
            "            \"timestamp\": \"2023-10-26T14:30:15Z\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"power\": {\n" +
            "              \"unit\": \"kW\",\n" +
            "              \"value\": 5.000\n" +
            "            },\n" +
            "            \"timestamp\": \"2023-10-26T14:30:15Z\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"storageCosts\": {\n" +
            "          \"variableCost\": {\n" +
            "            \"unit\": \"EUR/kWh\",\n" +
            "            \"value\": 0.050\n" +
            "          },\n" +
            "          \"costPerUsage\": {\n" +
            "            \"unit\": \"EUR/usage\",\n" +
            "            \"value\": 0.010\n" +
            "          },\n" +
            "          \"fixedCost\": {\n" +
            "            \"unit\": \"EUR\",\n" +
            "            \"value\": 10.000\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    ],\n" +
            "    \"dependencies\": [\n" +
            "      {\n" +
            "        \"dependencyId\": {\n" +
            "          \"uuid\": \"123e4567-e89b-12d3-a456-426614174000\",\n" +
            "          \"comment\": \"Dependency ID\"\n" +
            "        },\n" +
            "        \"triggeringFlexibleLoad\": {\n" +
            "          \"temporalType\": \"start\",\n" +
            "          \"triggeringFlexibleLoadId\": {\n" +
            "            \"uuid\": \"123e4567-e89b-12d3-a456-426614174001\",\n" +
            "            \"comment\": \"Triggering Load ID\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"targetFlexibleLoad\": {\n" +
            "          \"temporalType\": \"end\",\n" +
            "          \"targetFlexibleLoadId\": {\n" +
            "            \"uuid\": \"123e4567-e89b-12d3-a456-426614174002\",\n" +
            "            \"comment\": \"Target Load ID\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"logicalType\": \"implies\",\n" +
            "        \"applicabilityDuration\": {\n" +
            "          \"unit\": \"s\",\n" +
            "          \"minValue\": 0.500,\n" +
            "          \"maxValue\": 300.000\n" +
            "        },\n" +
            "        \"applicabilityConditions\": [\n" +
            "          {\n" +
            "            \"formulaLeft\": \"variableA\",\n" +
            "            \"formulaRight\": \"constant1\",\n" +
            "            \"comparator\": \"equals\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"formulaLeft\": \"variableB\",\n" +
            "            \"formulaRight\": \"constant2\",\n" +
            "            \"comparator\": \"lessEqual\"\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n";

    private static final String INVALID_JSON = "{\n" +
            "  \"metadata\": {\n" +
           // "    \"efdmVersion\": {\n" +
            "      \"versionNumber\": \"1.0.0\",\n" +
            "      \"schemaLink\": \"https://git.ptw.maschinenbau.tu-darmstadt.de/eta-fabrik/public/energy_flexibility_data_model/-/blob/main/efdm_v1.0/JSON_schema/schema_flexibility_space.json\"\n" +
            "    },\n" +
            "    \"origin\": {\n" +
            "      \"originId\": \"6e8bc430-9c3a-11d9-9669-0800200c9a67\",\n" +
            "      \"timestamp\": \"2023-10-26T14:30:15Z\"\n" +
            "    },\n" +
            "    \"modification\": {\n" +
            "      \"modifierId\": \"6e8bc430-9c3a-11d9-9669-0800200c9a68\",\n" +
            "      \"timestamp\": \"2023-10-26T15:30:15Z\"\n" +
            "    },\n" +
            "    \"comment\": \"This is a sample comment\"\n" +
            "  },\n" +
            "    \"flexibleLoads\": [\n" +
            "      {\n" +
            "        \"flexibleLoadId\": {\n" +
            "          \"uuid\": \"6e8bc430-9c3a-11d9-9669-0800200c9a69\",\n" +
            "          \"comment\": \"Flexible load UUID\"\n" +
            "        },\n" +
            "        \"reactionDuration\": {\n" +
            "          \"unit\": \"s\",\n" +
            "          \"value\": 5.000\n" +
            "        },\n" +
            "        \"validity\": [\n" +
            "          {\n" +
            "            \"from\": \"2023-10-26T14:00:00Z\",\n" +
            "            \"until\": \"2023-10-26T15:00:00Z\",\n" +
            "            \"temporalType\": \"start\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"powerStates\": {\n" +
            "          \"order\": \"chronological\",\n" +
            "          \"durationType\": \"deliveryDuration\",\n" +
            "          \"values\": [\n" +
            "            {\n" +
            "              \"power\": {\n" +
            "                \"unit\": \"kW\",\n" +
            "                \"minValue\": 1.000,\n" +
            "                \"maxValue\": 5.000\n" +
            "              },\n" +
            "              \"duration\": {\n" +
            "                \"unit\": \"s\",\n" +
            "                \"minValue\": 1.001,\n" +
            "                \"maxValue\": 10.000\n" +
            "              }\n" +
            "            }\n" +
            "          ]\n" +
            "        },\n" +
            "        \"usageNumber\": {\n" +
            "          \"unit\": \"None\",\n" +
            "          \"minValue\": 1,\n" +
            "          \"maxValue\": 5\n" +
            "        },\n" +
            "        \"modulationNumber\": 1,\n" +
            "        \"powerGradients\": {\n" +
            "          \"activationGradient\": {\n" +
            "            \"unit\": \"kW/s\",\n" +
            "            \"minValue\": 0.001,\n" +
            "            \"maxValue\": 1.000\n" +
            "          },\n" +
            "          \"modulationGradient\": {\n" +
            "            \"unit\": \"kW/s\",\n" +
            "            \"minValue\": 0.001,\n" +
            "            \"maxValue\": 1.000\n" +
            "          },\n" +
            "          \"deactivationGradient\": {\n" +
            "            \"unit\": \"kW/s\",\n" +
            "            \"minValue\": 0.001,\n" +
            "            \"maxValue\": 1.000\n" +
            "          }\n" +
            "        },\n" +
            "        \"regenerationDuration\": {\n" +
            "          \"unit\": \"s\",\n" +
            "          \"value\": 5.000\n" +
            "        },\n" +
            "        \"flexibleLoadCosts\": {\n" +
            "          \"variableCost\": {\n" +
            "            \"unit\": \"EUR/kWh\",\n" +
            "            \"value\": 0.010\n" +
            "          },\n" +
            "          \"costPerUsage\": {\n" +
            "            \"unit\": \"EUR/usage\",\n" +
            "            \"value\": 0.010\n" +
            "          },\n" +
            "          \"fixedCost\": {\n" +
            "            \"unit\": \"EUR\",\n" +
            "            \"value\": 10.000\n" +
            "          }\n" +
            "        },\n" +
            "        \"orderConfirmationDeadline\": {\n" +
            "          \"deadlineType\": \"relative\",\n" +
            "          \"deadlineValue\": {\n" +
            "            \"unit\": \"s\",\n" +
            "            \"value\": 60.000\n" +
            "          }\n" +
            "        },\n" +
            "        \"prices\": {\n" +
            "          \"variablePrice\": {\n" +
            "            \"unit\": \"EUR/kWh\",\n" +
            "            \"value\": 0.010\n" +
            "          },\n" +
            "          \"pricePerUsage\": {\n" +
            "            \"unit\": \"EUR/usage\",\n" +
            "            \"value\": 0.010\n" +
            "          },\n" +
            "          \"fixedPrice\": {\n" +
            "            \"unit\": \"EUR\",\n" +
            "            \"value\": 10.000\n" +
            "          }\n" +
            "        },\n" +
            "        \"location\": {\n" +
            "          \"meterLocation\": \"Location A\",\n" +
            "          \"voltageLevel\": {\n" +
            "            \"unit\": \"kV\",\n" +
            "            \"value\": 11.000\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    ],\n" +
            "    \"storages\": [\n" +
            "      {\n" +
            "        \"storageId\": {\n" +
            "          \"uuid\": \"6e8bc430-9c3a-11d9-9669-0800200c9a66\",\n" +
            "          \"comment\": \"Storage UUID\"\n" +
            "        },\n" +
            "        \"usableCapacity\": {\n" +
            "          \"unit\": \"kWh\",\n" +
            "          \"minValue\": 1.000,\n" +
            "          \"maxValue\": 10.000\n" +
            "        },\n" +
            "        \"initialEnergyContent\": {\n" +
            "          \"unit\": \"kWh\",\n" +
            "          \"minValue\": 2.000,\n" +
            "          \"maxValue\": 8.000\n" +
            "        },\n" +
            "        \"targetEnergyContent\": {\n" +
            "          \"unit\": \"kWh\",\n" +
            "          \"minValue\": 5.000,\n" +
            "          \"maxValue\": 9.000\n" +
            "        },\n" +
            "        \"energyLoss\": {\n" +
            "          \"unit\": \"%/h\",\n" +
            "          \"value\": 0.500\n" +
            "        },\n" +
            "        \"suppliers\": [\n" +
            "          {\n" +
            "            \"flexibleLoadId\": {\n" +
            "              \"uuid\": \"6e8bc430-9c3a-11d9-9669-0800200c9a69\",\n" +
            "              \"comment\": \"Supplier UUID\"\n" +
            "            },\n" +
            "            \"conversionEfficiency\": {\n" +
            "              \"unit\": \"%\",\n" +
            "              \"value\": 98.000\n" +
            "            }\n" +
            "          }\n" +
            "        ],\n" +
            "        \"drain\": [\n" +
            "          {\n" +
            "            \"power\": {\n" +
            "              \"unit\": \"kW\",\n" +
            "              \"value\": 5.000\n" +
            "            },\n" +
            "            \"timestamp\": \"2023-10-26T14:30:15Z\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"power\": {\n" +
            "              \"unit\": \"kW\",\n" +
            "              \"value\": 5.000\n" +
            "            },\n" +
            "            \"timestamp\": \"2023-10-26T14:30:15Z\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"storageCosts\": {\n" +
            "          \"variableCost\": {\n" +
            "            \"unit\": \"EUR/kWh\",\n" +
            "            \"value\": 0.050\n" +
            "          },\n" +
            "          \"costPerUsage\": {\n" +
            "            \"unit\": \"EUR/usage\",\n" +
            "            \"value\": 0.010\n" +
            "          },\n" +
            "          \"fixedCost\": {\n" +
            "            \"unit\": \"EUR\",\n" +
            "            \"value\": 10.000\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    ],\n" +
            "    \"dependencies\": [\n" +
            "      {\n" +
            "        \"dependencyId\": {\n" +
            "          \"uuid\": \"123e4567-e89b-12d3-a456-426614174000\",\n" +
            "          \"comment\": \"Dependency ID\"\n" +
            "        },\n" +
            "        \"triggeringFlexibleLoad\": {\n" +
            "          \"temporalType\": \"start\",\n" +
            "          \"triggeringFlexibleLoadId\": {\n" +
            "            \"uuid\": \"123e4567-e89b-12d3-a456-426614174001\",\n" +
            "            \"comment\": \"Triggering Load ID\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"targetFlexibleLoad\": {\n" +
            "          \"temporalType\": \"end\",\n" +
            "          \"targetFlexibleLoadId\": {\n" +
            "            \"uuid\": \"123e4567-e89b-12d3-a456-426614174002\",\n" +
            "            \"comment\": \"Target Load ID\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"logicalType\": \"implies\",\n" +
            "        \"applicabilityDuration\": {\n" +
            "          \"unit\": \"s\",\n" +
            "          \"minValue\": 0.500,\n" +
            "          \"maxValue\": 300.000\n" +
            "        },\n" +
            "        \"applicabilityConditions\": [\n" +
            "          {\n" +
            "            \"formulaLeft\": \"variableA\",\n" +
            "            \"formulaRight\": \"constant1\",\n" +
            "            \"comparator\": \"equals\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"formulaLeft\": \"variableB\",\n" +
            "            \"formulaRight\": \"constant2\",\n" +
            "            \"comparator\": \"lessEqual\"\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n";

}
