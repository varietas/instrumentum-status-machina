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
package io.varietas.instrumentum.status.machina.builder.impl;

import io.varietas.instrumentum.status.machina.ChainStateMachine;
import io.varietas.instrumentum.status.machina.StateMachine;
import io.varietas.instrumentum.status.machina.annotation.ChainListener;
import io.varietas.instrumentum.status.machina.annotation.ChainListeners;
import io.varietas.instrumentum.status.machina.annotation.StateMachineConfiguration;
import io.varietas.instrumentum.status.machina.annotation.TransitionChain;
import io.varietas.instrumentum.status.machina.annotation.TransitionChains;
import io.varietas.instrumentum.status.machina.configuration.CFSMConfigurationImpl;
import io.varietas.instrumentum.status.machina.container.ChainContainer;
import io.varietas.instrumentum.status.machina.container.ListenerContainer;
import io.varietas.instrumentum.status.machina.container.TransitionContainer;
import io.varietas.instrumentum.status.machina.error.TransitionChainCreationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>ChainStateMachineBuilderImpl</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 10/27/2017
 */
@Slf4j
public class ChainStateMachineBuilderImpl extends StateMachineBuilderImpl {

    protected Class<? extends Enum> chainType;

    private final List<ChainContainer> chains = new ArrayList<>();

    /**
     * Extracts the configuration from a given {@link StateMachine}. This process should be done only once per state machine type and shared between the instances because the collection of information
     * is a big process and can take a while.
     *
     * @param machineType State machine type where the configuration is present.
     *
     * @return The instance of the builder for a fluent like API.
     */
    @Override
    public ChainStateMachineBuilderImpl extractConfiguration(final Class<? extends StateMachine> machineType) {
        this.machineType = machineType;

        StateMachineConfiguration machineConfiguration = this.machineType.getAnnotation(StateMachineConfiguration.class);

        this.stateType = machineConfiguration.stateType();
        this.eventType = machineConfiguration.eventType();
        this.chainType = machineConfiguration.chainType();

        this.transitions.addAll(this.collectTransitions());
        this.chains.addAll(this.createChains());

        this.configuration = new CFSMConfigurationImpl(
                this.transitions,
                this.chains,
                this.stateType,
                this.eventType,
                this.chainType);

        LOGGER.debug("Configuration for '{}' created:\n"
                + "-> {} transitions collected\n"
                + "-> {} chains collected\n"
                + "-> {} used for state type\n"
                + "-> {} used for event type\n"
                + "-> {} used for chain type.",
                this.machineType.getSimpleName(),
                this.transitions.size(),
                this.chains.size(),
                this.stateType.getSimpleName(),
                this.eventType.getSimpleName(),
                this.chainType.getSimpleName()
        );

        return this;
    }

    /**
     * The varietas.io transition implementation supports transition chains. These chains allow the definition of transition on programming time. That makes execution of chained transitions with a
     * single command possible. This method creates all available transition chains.
     *
     * @return List of all available transition chains.
     */
    private List<ChainContainer> createChains() {

        final List<Pair> listeners = this.extractChainListener(this.machineType);

        if (!this.machineType.isAnnotationPresent(TransitionChains.class) && this.machineType.isAnnotationPresent(TransitionChains.class)) {
            return Collections.emptyList();
        }

        return Stream.of(this.machineType.getAnnotationsByType(TransitionChain.class))
                .map(chain -> {
                    List<ListenerContainer> requiredListeners = listeners.stream()
                            .filter(listener -> listener.targetChains.contains("ALL") || listener.targetChains.contains(chain.on()))
                            .map(listener -> listener.listener)
                            .collect(Collectors.toList());
                    return this.createChain(chain, requiredListeners);
                })
                .distinct()
                .collect(Collectors.toList());

    }

    /**
     * Creates a chain container with all required information.
     *
     * @param chain     Target chain of the container.
     * @param listeners Available listeners which have to be fired for this chain.
     *
     * @return Chain container with all relevant information.
     */
    private ChainContainer createChain(final TransitionChain chain, final List<ListenerContainer> listeners) {
        Enum from = Enum.valueOf(this.stateType, chain.from());
        Enum to = Enum.valueOf(stateType, chain.to());
        Enum on = Enum.valueOf(this.chainType, chain.on());
        Optional<List<TransitionContainer>> chainParts = this.recursive(from, to);

        if (!chainParts.isPresent()) {
            throw new TransitionChainCreationException(true, from.name(), to.name(), on.name());
        }

        ChainContainer res = new ChainContainer<>(
                from,
                to,
                on,
                chainParts.get(),
                listeners
        );

        LOGGER.debug("Chain {}: {} -> {}", res.getOn(), res.getFrom(), res.getTo());
        res.getChainParts().forEach(part -> {
            TransitionContainer partContainer = ((TransitionContainer) part);
            LOGGER.debug("  - {}: {} -> {}", partContainer.getOn(), partContainer.getFrom(), partContainer.getTo());
        });
        LOGGER.debug("{} listeners for chain {} added.", (Objects.nonNull(res.getListeners()) ? res.getListeners().size() : 0), res.getOn());

        return res;
    }

    /**
     * Extracts all chain listeners from a given {@link ChainStateMachine}.
     *
     * @param type Chain state machine type.
     *
     * @return Collected chain listeners as list.
     */
    private List<Pair> extractChainListener(final Class<?> type) {
        if (!type.isAnnotationPresent(ChainListeners.class) && !type.isAnnotationPresent(ChainListener.class)) {
            return Collections.EMPTY_LIST;
        }

        return Stream.of(type.getAnnotationsByType(ChainListener.class))
                .map(annot -> {
                    Class<?> listener = annot.value();
                    ListenerContainer listenerContainer = new ListenerContainer(listener, this.existsMethod(listener, "before"), this.existsMethod(listener, "after"));
                    return new Pair(listenerContainer, Arrays.asList(annot.forChains()));
                })
                .collect(Collectors.toList());
    }

    /**
     * Collects all transitions required by transition chain from the already collected transitions.
     *
     * @param from Start state of the transition chain.
     * @param to   End state of the transition chain.
     *
     * @return List of all required transitions as containers.
     */
    private Optional<List<TransitionContainer>> recursive(final Enum from, final Enum to) {

        List<TransitionContainer> possibleParts = this.transitions.stream()
                .filter(transition -> transition.getFrom().equals(from))
                .collect(Collectors.toList());

        return possibleParts.stream()
                .map((possiblePart) -> {
                    List<TransitionContainer> temp = new ArrayList<>();
                    temp.add(possiblePart);
                    if (!this.recursive(to, possiblePart, temp, 1)) {
                        return null;
                    }
                    return temp;
                })
                .filter(Objects::nonNull)
                .min(Comparator.comparingInt(List::size));
    }

    /**
     * Collects all transitions required by transition chain from the already collected transitions in a recursively way. This method searches a way from the start state to the end state.
     *
     * @param abourt       End state of the chain.
     * @param possiblePart Currently used start transition.
     * @param chainParts   List of all collected transitions.
     * @param fallback     Abort criteria. This is simply a counter which is increased each recursive step. If the counter greater than the current number of available transitions, the algorithm
     *                     detects no possible way from the start to the end.
     *
     * @return True if the end state is located, otherwise false.
     */
    private boolean recursive(final Enum abourt, final TransitionContainer possiblePart, final List<TransitionContainer> chainParts, final int fallBack) {

        if (possiblePart.getTo().equals(abourt)) {
            return true;
        }

        if (fallBack > this.transitions.size()) {
            return false;
        }

        List<TransitionContainer> nextPossibles = this.transitions.stream()
                .filter(transition -> transition.getFrom().equals(possiblePart.getTo()))
                .collect(Collectors.toList());

        if (nextPossibles.isEmpty()) {
            LOGGER.trace("There is no transition available from '{}' to '{}'.", possiblePart.getFrom().name(), abourt.name());
            return false;
        }

        if (nextPossibles.size() == 1) {
            List<TransitionContainer> temp = new ArrayList<>();

            TransitionContainer toAdd = nextPossibles.get(0);

            chainParts.add(toAdd);
            if (!this.recursive(abourt, toAdd, temp, fallBack + 1)) {
                return false;
            }

            chainParts.addAll(temp);
            return true;
        }

        final List<List<TransitionContainer>> buffer = nextPossibles.stream()
                .map((nextPossible) -> {
                    List<TransitionContainer> temp = new ArrayList<>();
                    temp.add(nextPossible);
                    if (!this.recursive(abourt, nextPossible, temp, fallBack + 1)) {
                        return null;
                    }
                    return temp;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!buffer.isEmpty()) {
            ///< No parts found
            chainParts.addAll(buffer.stream().min(Comparator.comparingInt(List::size)).get());
            return true;
        }

        return false;
    }

    @AllArgsConstructor
    private static class Pair {

        public final ListenerContainer listener;
        public final List<String> targetChains;
    }
}
