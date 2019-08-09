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

import io.varietas.instrumentum.status.machina.StateMachine;
import io.varietas.instrumentum.status.machina.annotations.TransitionChain;
import io.varietas.instrumentum.status.machina.builders.SimpleStateMachineBuilder;
import io.varietas.instrumentum.status.machina.error.UnexpectedArgumentException;
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
 * <h2>ChainContainer</h2>
 * <p>
 * The chain container holds relevant information collected from {@link TransitionChain} annotation on runtime. This includes the
 * <ul>
 * <li>required start state,</li>
 * <li>state after transition,</li>
 * <li>name of the chain and</li>
 * <li>the list of transitions, which are passed.</li>
 * </ul>
 * <p>
 * The transitions are collected while the configuration is created automatically by the {@link SimpleStateMachineBuilder}. Its recommended that a configuration is created once per {@link StateMachine} and shared between the instances.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/7/2017
 * @param <STATE_TYPE>      Generic type of enumeration which is used to represent the states.
 * @param <TRANSITION_TYPE> Generic type of enumeration which is used to represent the occurred event (Event identifier).
 * @param <CHAIN_TYPE>      Generic type of enumeration which is used to represent a chain event (Chain identifier).
 */
@ToString(exclude = {"from", "to", "chainParts"})
@EqualsAndHashCode(exclude = "chainParts")
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChainContainer<STATE_TYPE extends Enum, TRANSITION_TYPE extends Enum, CHAIN_TYPE extends Enum> {

    STATE_TYPE from;

    STATE_TYPE to;

    CHAIN_TYPE on;

    List<TransitionContainer> chainParts;

    List<ListenerContainer> listeners;

    /**
     * Creates a {@link ChainContainer} with the basic information. Adding chain parts and listeners is possible by {@link ChainContainer#andAdd(java.lang.Object)} or {@link ChainContainer#andAddAll(java.util.List)}.
     *
     * @param from Type that marks the start of the chain
     * @param to   Type that marks the end of the chain
     * @param on   Type that is used as the identifier of the chain
     *
     * @return An instance with the basic information of the chain
     */
    public static ChainContainer of(@NonNull final Enum from, @NonNull final Enum to, @NonNull final Enum on) {
        return new ChainContainer(from, to, on, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Adds a container to the corresponding container list. Supported types are:
     * <ul>
     * <li>{@link TransitionChain}</li>
     * <li>{@link ListenerContainer}</li>
     * </ul>
     *
     * @param container Container that has to be added to its corresponding list
     *
     * @return The instance of this container for a fluent like usage.
     */
    public ChainContainer andAdd(@NonNull final Object container) {
        final boolean isTransitionContainer = TransitionContainer.class.isInstance(container);
        final boolean isListenerContainer = ListenerContainer.class.isInstance(container);

        if (!isTransitionContainer && !isListenerContainer) {
            throw new UnexpectedArgumentException(container, "Given object is not instance of " + TransitionContainer.class.getCanonicalName() + " or " + ListenerContainer.class.getCanonicalName() + ".");
        }

        if (isTransitionContainer) {
            this.chainParts.add((TransitionContainer) container);
        } else {
            this.listeners.add((ListenerContainer) container);
        }

        return this;
    }

    /**
     * Adds a list of containers to the corresponding container list. Supported types are:
     * <ul>
     * <li>{@link TransitionChain}</li>
     * <li>{@link ListenerContainer}</li>
     * </ul>
     *
     * @param <LIST>     Generic type of container list
     * @param containers List of container that have to be added to its corresponding list
     *
     * @return The instance of this container for a fluent like usage.
     */
    public <LIST extends List<?>> ChainContainer andAddAll(final LIST containers) {

        if (Objects.isNull(containers) || containers.isEmpty()) {
            return this;
        }

        final boolean isTransitionContainer = TransitionContainer.class.isInstance(containers.get(0));
        final boolean isListenerContainer = ListenerContainer.class.isInstance(containers.get(0));

        if (!isTransitionContainer && !isListenerContainer) {
            throw new UnexpectedArgumentException(containers, "Given object is not instance of " + TransitionContainer.class.getCanonicalName() + " or " + ListenerContainer.class.getCanonicalName());
        }

        if (isTransitionContainer) {
            this.chainParts.addAll((List<? extends TransitionContainer>) containers);
        } else {
            this.listeners.addAll((List<? extends ListenerContainer>) containers);
        }

        return this;
    }
}
