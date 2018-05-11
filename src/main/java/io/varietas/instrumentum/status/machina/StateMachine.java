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
 * <h2>StateMachine</h2>
 * <p>
 * This interface has to be implemented by each finite state machine. It represents the minimal API of a FSM and makes the usage within DI frameworks e.g. Spring or agrestis imputare possible.
 *
 * @author Michael Rhöse
 * @version 1.0.0, 10/9/2017
 */
@FunctionalInterface
public interface StateMachine {

    /**
     * Executes the corresponding logic for a upcoming event on the given object. The first step is the comparison of the current state of the object with the required state of the transition. If an
     * object isn't in the right state, an {@link InvalidTransitionException} is thrown.
     *
     * @param transition Upcoming transition event that triggers the FSM.
     * @param target     Transition operation target.
     *
     * @throws TransitionInvocationException Thrown if the upcoming transition event isn't configured for the current FSM.
     * @throws InvalidTransitionException Thrown if the current state of a target isn't equals to the expected transition start state.
     */
    void fire(final Enum transition, final StatedObject target) throws TransitionInvocationException, InvalidTransitionException;
}
