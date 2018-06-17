/*
 * Copyright 2017 Michael Rhöse.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.varietas.instrumentum.status.machina.builders;

import io.varietas.instrumentum.status.machina.StateMachine;
import io.varietas.instrumentum.status.machina.configuration.FSMConfiguration;
import io.varietas.instrumentum.status.machina.error.MachineCreationException;

/**
 * <h2>StateMachineBuilder</h2>
 * <p>
 * The state machine builder is the way for creating an FSM instance. The builder scans the FSM type for CbA and returns a fully instantiated FSM that is ready to use. There are different
 * implementations of the configuration builder available. Each implementation allows the overwriting for custom FSM and custom FSM builder.
 *
 * @author Michael Rhöse
 * @version 1.0.0, 10/31/2017
 * @param <CONFIGURATION> Generic type of configuration that is created by the builder.
 */
public interface StateMachineBuilder<CONFIGURATION extends FSMConfiguration> {

    /**
     * Adds the FSM type for extracting of the configuration.
     *
     * @param machineType The FSM type.
     *
     * @return The instance of the builder for fluent like usage.
     */
    StateMachineBuilder<CONFIGURATION> extractConfiguration(final Class<? extends StateMachine> machineType);

    /**
     * Instantiates the FSM with the extracted configuration and returns them.
     *
     * @return The FSM instance.
     *
     * @throws MachineCreationException Thrown for all possible exceptions while configuration creation.
     */
    StateMachine build() throws MachineCreationException;

    /**
     * Gets the extracted configuration of the current FSM.
     *
     * @return The FSM configuration.
     */
    CONFIGURATION configuration();
}
