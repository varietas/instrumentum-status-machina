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
package io.varietas.instrumentum.status.machina.containers;

import io.varietas.instrumentum.status.machina.annotations.Transition;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * <h2>TransitionContainer</h2>
 * <p>
 * The transition container holds relevant information collected from {@link Transition} annotation on runtime. This includes the
 * <ul>
 * <li>required start state,</li>
 * <li>state after transition,</li>
 * <li>name of the transition and</li>
 * <li>method which is invoked to manipulate the transition target.</li>
 * </ul>
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/7/2017
 * @param <STATE_TYPE> Generic type of enumeration which is used to represent the states.
 * @param <TRANSITION_TYPE> Generic type of enumeration which is used to represent the occurred event (Event identifier).
 */
@ToString(exclude = {"calledMethod"})
@EqualsAndHashCode(exclude = "calledMethod")
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("rawtypes")
public class TransitionContainer<STATE_TYPE extends Enum, TRANSITION_TYPE extends Enum> {

    STATE_TYPE from;

    STATE_TYPE to;

    TRANSITION_TYPE on;

    Method calledMethod;

    List<ListenerContainer> listeners;

    /**
     * Creates a {@link TransitionContainer} with the basic information. Adding listeners is possible by
     * {@link TransitionContainer#andAddListener(io.varietas.instrumentum.status.machina.containers.ListenerContainer)} or {@link TransitionContainer#andAddListeners(java.util.List)}.
     *
     * @param from Type that marks the start of the transition
     * @param to Type that marks the end of the transition
     * @param on Type that is used as the identifier of the transition
     * @param calledMethod The method that represents the handler of the call
     * @param <STATE_TYPE> Generic type of enumeration which is used to represent the states.
     * @param <TRANSITION_TYPE> Generic type of enumeration which is used to represent the occurred event (Event identifier).
     *
     * @return An instance with the basic information of the transition
     */
    public static <STATE_TYPE extends Enum<?>, TRANSITION_TYPE extends Enum<?>> TransitionContainer<STATE_TYPE, TRANSITION_TYPE> of(@NonNull final STATE_TYPE from, @NonNull final STATE_TYPE to, @NonNull final TRANSITION_TYPE on, @NonNull final Method calledMethod) {
        return new TransitionContainer<>(from, to, on, calledMethod, new ArrayList<>());
    }

    /**
     * Adds a listener to the transition.
     *
     * @param container Container that has to be added to the transition
     *
     * @return The instance of this container for a fluent like usage.
     */
    public TransitionContainer<STATE_TYPE, TRANSITION_TYPE> andAddListener(@NonNull final ListenerContainer container) {

        listeners.add(container);

        return this;
    }

    /**
     * Adds a list of listeners to the transition.
     *
     * @param containers List of listeners that have to be added to the transition
     *
     * @return The instance of this container for a fluent like usage.
     */
    public TransitionContainer<STATE_TYPE, TRANSITION_TYPE> andAddListeners(final List<ListenerContainer> containers) {

        if (Objects.isNull(containers) || containers.isEmpty()) {
            return this;
        }

        this.listeners.addAll(containers);

        return this;
    }
}
