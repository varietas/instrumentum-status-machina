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
package io.varietas.instrumentum.status.machina.configurations;

import io.varietas.instrumentum.status.machina.StateMachine;
import io.varietas.instrumentum.status.machina.containers.TransitionContainer;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * <h2>DefaultFSMConfiguration</h2>
 * <p>
 * This class represents a container to use FSM in a dependency injection framework like agrestis imputare. It allows the separate storing of configuration as singleton.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/10/2017
 */
@ToString
@EqualsAndHashCode
@Value
@NonFinal
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DefaultFSMConfiguration implements FSMConfiguration {

    private final Class<? extends StateMachine> machineType;

    private final List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> transitions;

    private final Class<? extends Enum<?>> stateType;

    private final Class<? extends Enum<?>> eventType;

    public static DefaultFSMConfiguration of(Class<? extends StateMachine> machineType, final Class<? extends Enum<?>> stateType, final Class<? extends Enum<?>> eventType) {

        return new DefaultFSMConfiguration(machineType, new ArrayList<>(), stateType, eventType);
    }

    /**
     * Adds a transition to the FSM configuration.
     *
     * @param container Container that has to be added to the configuration
     *
     * @return The instance of this container for a fluent like usage.
     */
    public DefaultFSMConfiguration andAddTransition(@NonNull final TransitionContainer<? extends Enum<?>, ? extends Enum<?>> container) {

        this.transitions.add(container);

        return this;
    }

    /**
     * Adds a list of transitions to the FSM configuration.
     *
     * @param containers List of transitions that have to be added to the configuration
     *
     * @return The instance of this container for a fluent like usage.
     */
    public DefaultFSMConfiguration andAddTransitions(@NonNull final List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> containers) {

        if (containers.isEmpty()) {
            throw new NullPointerException("Empty list isn't allowed");
        }

        this.transitions.addAll(containers);

        return this;
    }
}
