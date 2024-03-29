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
import io.varietas.instrumentum.status.machina.errors.UnexpectedArgumentException;
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
 * The transitions are collected while the configuration is created automatically by the {@link SimpleStateMachineBuilder}. Its recommended that a configuration is created once per
 * {@link StateMachine} and shared between the instances.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/7/2017
 * @param <STATE_TYPE> Generic type of enumeration which is used to represent the states.
 * @param <TRANSITION_TYPE> Generic type of enumeration which is used to represent the occurred event (Event identifier).
 * @param <CHAIN_TYPE> Generic type of enumeration which is used to represent a chain event (Chain identifier).
 */
@ToString(exclude = {"from", "to", "chainParts"})
@EqualsAndHashCode(exclude = "chainParts")
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChainContainer<STATE_TYPE extends Enum<?>, TRANSITION_TYPE extends Enum<?>, CHAIN_TYPE extends Enum<?>> {

    STATE_TYPE from;

    STATE_TYPE to;

    CHAIN_TYPE on;

    List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> chainParts;

    List<ListenerContainer> listeners;

    /**
     * Creates a {@link ChainContainer} with the basic information. Adding chain parts and listeners is possible by {@link ChainContainer#andAdd(java.lang.Object)} or
     * {@link ChainContainer#andAddAll(java.util.List)}.
     *
     * @param from Type that marks the start of the chain
     * @param to Type that marks the end of the chain
     * @param on Type that is used as the identifier of the chain
     * @param <STATE_TYPE> Generic type of enumeration which is used to represent the states.
     * @param <TRANSITION_TYPE> Generic type of enumeration which is used to represent the occurred event (Event identifier).
     * @param <CHAIN_TYPE> Generic type of enumeration which is used to represent a chain event (Chain identifier).
     *
     * @return An instance with the basic information of the chain
     */
    public static <STATE_TYPE extends Enum<?>, TRANSITION_TYPE extends Enum<?>, CHAIN_TYPE extends Enum<?>> ChainContainer<STATE_TYPE, TRANSITION_TYPE, CHAIN_TYPE> of(@NonNull final STATE_TYPE from, @NonNull STATE_TYPE to, @NonNull final CHAIN_TYPE on) {
        return new ChainContainer<>(from, to, on, new ArrayList<>(), new ArrayList<>());
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
    @SuppressWarnings("unchecked")
    public ChainContainer<STATE_TYPE, TRANSITION_TYPE, CHAIN_TYPE> andAdd(@NonNull final Object container) {

        return this.checkSupportedTypesAndCallFunction(container);
    }

    /**
     * Adds a list of containers to the corresponding container list. Supported types are:
     * <ul>
     * <li>{@link TransitionChain}</li>
     * <li>{@link ListenerContainer}</li>
     * </ul>
     *
     * @param <LIST> Generic type of container list
     * @param containers List of container that have to be added to its corresponding list
     *
     * @return The instance of this container for a fluent like usage.
     */
    @SuppressWarnings("unchecked")
    public <LIST extends List<?>> ChainContainer<STATE_TYPE, TRANSITION_TYPE, CHAIN_TYPE> andAddAll(final LIST containers) {

        return this.checkSupportedTypesAndCallFunction(containers);
    }

    @SuppressWarnings("unchecked")
    private ChainContainer<STATE_TYPE, TRANSITION_TYPE, CHAIN_TYPE> checkSupportedTypesAndCallFunction(final Object any) {

        if (Objects.isNull(any)) {
            return this;
        }

        final boolean isList = List.class.isInstance(any);

        if (isList && ((List) any).isEmpty()) {
            return this;
        }

        final Object used = (isList) ? ((List) any).get(0) : any;

        final boolean isTransitionContainer = TransitionContainer.class.isInstance(used);
        final boolean isListenerContainer = ListenerContainer.class.isInstance(used);

        if (!isTransitionContainer && !isListenerContainer) {
            throw new UnexpectedArgumentException(used, "Given object is not instance of " + TransitionContainer.class.getCanonicalName() + " or " + ListenerContainer.class.getCanonicalName());
        }

        if (isTransitionContainer) {
            if (isList) {
                this.chainParts.addAll((List<? extends TransitionContainer<STATE_TYPE, TRANSITION_TYPE>>) any);
            } else {
                this.chainParts.add((TransitionContainer<STATE_TYPE, TRANSITION_TYPE>) any);
            }
        } else {
            if (isList) {
                this.listeners.addAll((List) any);
            } else {
                this.listeners.add((ListenerContainer) any);
            }
        }
        return this;
    }
}
