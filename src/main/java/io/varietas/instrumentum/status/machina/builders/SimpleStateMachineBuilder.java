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
import io.varietas.instrumentum.status.machina.annotations.StateMachineConfiguration;
import io.varietas.instrumentum.status.machina.configuration.DefaultFSMConfiguration;
import io.varietas.instrumentum.status.machina.configuration.FSMConfiguration;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>StateMachineBuilderImpl</h2>
 * <p>
 * Implementation of the {@link StateMachineBuilder} interface. This builder is for simple state machines.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/9/2017
 */
@Slf4j
@NoArgsConstructor(staticName = "getBuilder")
public class SimpleStateMachineBuilder extends BasicStateMachineBuilder<FSMConfiguration> {

    /**
     * Extracts the configuration from a given {@link StateMachine}. This process should be done only once per state machine type and shared between the instances because the collection of information is a big process and can take a while.
     *
     * @param machineType State machine type where the configuration is present.
     *
     * @return The instance of the builder for a fluent like API.
     */
    @Override
    public StateMachineBuilder<FSMConfiguration> extractConfiguration(@NonNull final Class<? extends StateMachine> machineType) {

        StateMachineConfiguration machineConfiguration = machineType.getAnnotation(StateMachineConfiguration.class);

        this.stateType = machineConfiguration.stateType();
        this.eventType = machineConfiguration.eventType();

        this.transitions.addAll(this.collectTransitions(machineType));

        this.configuration = DefaultFSMConfiguration.of(machineType, this.stateType, this.eventType).andAddTransitions(this.transitions);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Configuration for '{}' created:\n"
                    + "-> {} transitions collected\n"
                    + "-> {} used for state type\n"
                    + "-> {} used for event type.",
                    this.configuration.getMachineType().getSimpleName(),
                    this.transitions.size(),
                    this.stateType.getSimpleName(),
                    this.eventType.getSimpleName()
            );
        }

        return this;
    }
}
