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
package io.varietas.instrumentum.status.machina.configuration;

import io.varietas.instrumentum.status.machina.StateMachine;
import io.varietas.instrumentum.status.machina.containers.TransitionContainer;
import java.util.List;

/**
 * <h2>FSMConfiguration</h2>
 * <p>
 * This interface must be implemented by a container that stores the configuration for a {@link io.varietas.instrumentum.status.machina.StateMachine}. It contains the basic information that are necessary for a state machine.
 *
 * @see io.varietas.instrumentum.status.machina.StateMachine
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/27/2017
 */
public interface FSMConfiguration {

    /**
     * Returns the configured machine type which is used to instantiate the machine.
     *
     * @return Configured machine type.
     */
    Class<? extends StateMachine> getMachineType();

    /**
     * Returns the collected transitions of the FSM type.
     *
     * @return Collected transitions.
     */
    List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> getTransitions();

    /**
     * Returns the state type of the current FSM type.
     *
     * @return State type.
     */
    Class<? extends Enum<?>> getStateType();

    /**
     * Returns the event type of the current FSM type.
     *
     * @return event type.
     */
    Class<? extends Enum<?>> getEventType();
}
