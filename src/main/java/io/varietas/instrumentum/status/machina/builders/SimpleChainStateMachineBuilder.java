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

import io.varietas.instrumentum.status.machina.ChainStateMachine;
import io.varietas.instrumentum.status.machina.StateMachine;
import io.varietas.instrumentum.status.machina.annotations.ChainListener;
import io.varietas.instrumentum.status.machina.annotations.ChainListeners;
import io.varietas.instrumentum.status.machina.annotations.StateMachineConfiguration;
import io.varietas.instrumentum.status.machina.annotations.TransitionChain;
import io.varietas.instrumentum.status.machina.annotations.TransitionChains;
import io.varietas.instrumentum.status.machina.configurations.CFSMConfiguration;
import io.varietas.instrumentum.status.machina.configurations.DefaultCFSMConfiguration;
import io.varietas.instrumentum.status.machina.containers.ChainContainer;
import io.varietas.instrumentum.status.machina.containers.ListenerContainer;
import io.varietas.instrumentum.status.machina.containers.TransitionContainer;
import io.varietas.instrumentum.status.machina.errors.TransitionChainCreationException;
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
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>ChainStateMachineBuilderImpl</h2>
 * <p>
 * Implementation of the {@link StateMachineBuilder} interface. This builder is for state machines that provide transition chains.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/27/2017
 */
@Slf4j
@NoArgsConstructor(staticName = "getBuilder")
public class SimpleChainStateMachineBuilder extends BasicStateMachineBuilder<CFSMConfiguration> {

    @SuppressWarnings("rawtypes")
    protected Class<? extends Enum<?>> chainType;

    private final List<ChainContainer<? extends Enum<?>, ? extends Enum<?>, ? extends Enum<?>>> chains = new ArrayList<>();

    /**
     * Extracts the configuration from a given {@link StateMachine}. This process should be done only once per state machine type and shared between the instances because the collection of information is a big process and can take a while.
     *
     * @param machineType State machine type where the configuration is present.
     *
     * @return The instance of the builder for a fluent like API.
     */
    @Override
    public StateMachineBuilder<CFSMConfiguration> extractConfiguration(final Class<? extends StateMachine> machineType) {

        StateMachineConfiguration machineConfiguration = machineType.getAnnotation(StateMachineConfiguration.class);

        this.stateType = machineConfiguration.stateType();
        this.eventType = machineConfiguration.eventType();
        this.chainType = machineConfiguration.chainType();

        this.transitions.addAll(this.collectTransitions(machineType));
        this.chains.addAll(this.createChains(machineType));

        this.configuration = DefaultCFSMConfiguration.of(machineType, this.stateType, this.eventType, this.chainType)
                .andAddChains(this.chains)
                .andAddTransitions(this.transitions);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Configuration for '{}' created:\n"
                    + "-> {} transitions collected\n"
                    + "-> {} chains collected\n"
                    + "-> {} used for state type\n"
                    + "-> {} used for event type\n"
                    + "-> {} used for chain type.",
                    this.configuration.getMachineType().getSimpleName(),
                    this.transitions.size(),
                    this.chains.size(),
                    this.stateType.getSimpleName(),
                    this.eventType.getSimpleName(),
                    this.chainType.getSimpleName()
            );
        }

        return this;
    }

    /**
     * The varietas.io transition implementation supports transition chains. These chains allow the definition of transition on programming time. That makes execution of chained transitions with a single command possible. This method creates all available transition chains.
     *
     * @param machineType The machine where the chains are configured.
     *
     * @return List of all available transition chains.
     */
    private List<ChainContainer<? extends Enum<?>, ? extends Enum<?>, ? extends Enum<?>>> createChains(final Class<? extends StateMachine> machineType) {

        if (!machineType.isAnnotationPresent(TransitionChain.class) && !machineType.isAnnotationPresent(TransitionChains.class)) {
            return Collections.emptyList();
        }

        final List<Pair> listeners = this.extractChainListener(machineType);

        return Stream.of(machineType.getAnnotationsByType(TransitionChain.class))
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
    private ChainContainer<? extends Enum<?>, ? extends Enum<?>, ? extends Enum<?>> createChain(final TransitionChain chain, final List<ListenerContainer> listeners) {
        @SuppressWarnings("rawtypes")
        final Class<? extends Enum> stateClazzType = this.stateType;
        @SuppressWarnings("rawtypes")
        final Class<? extends Enum> chainClazzType = this.chainType;

        @SuppressWarnings("unchecked")
        final Enum<?> from = Enum.valueOf(stateClazzType, chain.from());
        @SuppressWarnings("unchecked")
        final Enum<?> to = Enum.valueOf(stateClazzType, chain.to());
        @SuppressWarnings("unchecked")
        final Enum<?> on = Enum.valueOf(chainClazzType, chain.on());
        Optional<List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>>> chainParts = this.recursive(from, to);

        if (!chainParts.isPresent()) {
            throw new TransitionChainCreationException(true, from.name(), to.name(), on.name());
        }

        @SuppressWarnings("unchecked")
        final ChainContainer<? extends Enum<?>, ? extends Enum<?>, ? extends Enum<?>> res = ChainContainer.of(from, to, on)
                .andAddAll(chainParts.get())
                .andAddAll(listeners);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Chain {}: {} -> {}", res.getOn(), res.getFrom(), res.getTo());
        }
        res.getChainParts().forEach(part -> {
            @SuppressWarnings("unchecked")
            TransitionContainer<? extends Enum<?>, ? extends Enum<?>> partContainer = part;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("  - {}: {} -> {}", partContainer.getOn(), partContainer.getFrom(), partContainer.getTo());
            }
        });

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("{} listeners for chain {} added.", (Objects.nonNull(res.getListeners()) ? res.getListeners().size() : 0), res.getOn());
        }

        return res;
    }

    /**
     * Extracts all chain listeners from a given {@link ChainStateMachine}.
     *
     * @param type Chain state machine type.
     *
     * @return Collected chain listeners as list.
     */
    @SuppressWarnings("unchecked")
    private List<Pair> extractChainListener(final Class<?> type) {
        if (!type.isAnnotationPresent(ChainListeners.class) && !type.isAnnotationPresent(ChainListener.class)) {
            return Collections.EMPTY_LIST;
        }

        return Stream.of(type.getAnnotationsByType(ChainListener.class))
                .map(annot -> {
                    final Class<?> listener = annot.value();
                    final ListenerContainer listenerContainer = ListenerContainer.of(listener, this.existsMethod(listener, "before"), this.existsMethod(listener, "after"));
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
    private Optional<List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>>> recursive(final Enum<?> from, final Enum<?> to) {

        List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> possibleParts = this.transitions.stream()
                .filter(transition -> transition.getFrom().equals(from))
                .collect(Collectors.toList());

        return possibleParts.stream()
                .map((possiblePart) -> {
                    List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> temp = new ArrayList<>();
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
     * @param fallback     Abort criteria. This is simply a counter which is increased each recursive step. If the counter greater than the current number of available transitions, the algorithm detects no possible way from the start to the end.
     *
     * @return True if the end state is located, otherwise false.
     */
    private boolean recursive(final Enum<?> abourt, final TransitionContainer<? extends Enum<?>, ? extends Enum<?>> possiblePart, final List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> chainParts, final int fallBack) {

        if (possiblePart.getTo().equals(abourt)) {
            return true;
        }

        if (fallBack > this.transitions.size()) {
            return false;
        }

        final List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> nextPossibles = this.transitions.stream()
                .filter(transition -> transition.getFrom().equals(possiblePart.getTo()))
                .collect(Collectors.toList());

        if (nextPossibles.isEmpty()) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("There is no transition available from '{}' to '{}'.", possiblePart.getFrom().name(), abourt.name());
            }
            return false;
        }

        if (nextPossibles.size() == 1) {
            List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> temp = new ArrayList<>();

            TransitionContainer<? extends Enum<?>, ? extends Enum<?>> toAdd = nextPossibles.get(0);

            chainParts.add(toAdd);
            if (!this.recursive(abourt, toAdd, temp, fallBack + 1)) {
                return false;
            }

            chainParts.addAll(temp);
            return true;
        }

        final List<List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>>> buffer = nextPossibles.stream()
                .map((nextPossible) -> {
                    List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> temp = new ArrayList<>();
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
