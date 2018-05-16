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
package io.varietas.instrumentum.status.machina;

import io.varietas.instrumentum.status.machina.error.InvalidTransitionException;
import io.varietas.instrumentum.status.machina.error.TransitionInvocationException;

/**
 * <h2>ChainStateMachine</h2>
 * <p>
 * This interface has to be implemented by each finite state machine that provides the chaining of transition. It represents the minimal API of a FSM and makes the usage within DI frameworks e.g.
 * Spring or agrestis imputare possible.
 *
 * <h3>Getting instance</h3>
 * Status machina (SM) extracts the configuration from a machine class and invokes the transition methods respectively the chain on the machine instance. For creating an instance, the
 * {@link io.varietas.instrumentum.status.machina.builders.StateMachineBuilder} is the best way. There are different implementations of the builder available. The taken builder depends on the state
 * machine type.
 *
 * <pre>
 * <code>
 *  CustomStateMachine machine = new ChainStateMachineBuilderImpl()
 *      .extractConfiguration(CustomStateMachine.class)
 *      .build();
 * </code>
 * </pre>
 *
 * The {@link io.varietas.instrumentum.status.machina.configuration.CFSMConfiguration} can be stored separately for instancing multiple machines.
 *
 * <pre>
 * <code>
 *  CFSMConfiguration configuration = new ChainStateMachineBuilderImpl()
 *      .extractConfiguration(CustomStateMachine.class)
 *      .configuration();
 *
 *  CustomStateMachine machine = new ChainStateMachineBuilderImpl().configuration(configuration).build()
 * </code>
 * </pre>
 *
 * An example for a basic state machine can be found in the test package (machines/transition/StateMachineWithoutListener.java).
 *
 * @see io.varietas.instrumentum.status.machina.builders.StateMachineBuilder
 *
 * @author Michael Rhöse
 * @version 1.0.0, 10/27/2017
 */
public interface ChainStateMachine extends StateMachine {

    /**
     * Executes the actual logic for a upcoming event on the given object. The first step is the comparison of the current state of the object with the required state of the transition. If an object
     * isn't in the right state, an {@link InvalidTransitionException} is thrown. The state machine invokes every transition that are specified for the chain event. Important: The transitions running
     * one after another.
     *
     * @param transitionChain Upcoming transition chain event that triggers the FSM.
     * @param target          Transition operation target.
     *
     * @throws TransitionInvocationException Thrown if the upcoming transition and/or chain event isn't configured for the current FSM.
     * @throws InvalidTransitionException Thrown if the current state of a target isn't equals to the expected transition start state.
     */
    void fireChain(final Enum transitionChain, final StatedObject target) throws TransitionInvocationException, InvalidTransitionException;
}
