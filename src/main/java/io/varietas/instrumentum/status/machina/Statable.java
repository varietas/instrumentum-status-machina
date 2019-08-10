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

/**
 * <h2>StatedObject</h2>
 * <p>
 * This interface must be implemented by each type that wants to be handled by a FSM. The stated object does not persist changes.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/8/2017
 * @param <STATE_TYPE> Generic type of state types.
 */
@SuppressWarnings("rawtypes")
public interface Statable<STATE_TYPE extends Enum> {

    /**
     * The state signals the FSM which transition is possible and which current state an object has.
     *
     * @return Current state of the object.
     */
    STATE_TYPE state();

    /**
     * Sets the new state for the current object. This method should only be used by FSM!
     *
     * @param state New state of current object.
     */
    void state(STATE_TYPE state);
}
