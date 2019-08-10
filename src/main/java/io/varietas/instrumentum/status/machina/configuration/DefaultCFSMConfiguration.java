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
import io.varietas.instrumentum.status.machina.containers.ChainContainer;
import io.varietas.instrumentum.status.machina.containers.TransitionContainer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * <h2>CFSMConfigurationImpl</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/27/2017
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Value
public class DefaultCFSMConfiguration extends DefaultFSMConfiguration implements CFSMConfiguration {

    private final List<ChainContainer<? extends Enum<?>, ? extends Enum<?>, ? extends Enum<?>>> chains;

    private final Class<? extends Enum<?>> chainType;

    private DefaultCFSMConfiguration(
            final Class<? extends StateMachine> machineType,
            final List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> transitions,
            final List<ChainContainer<? extends Enum<?>, ? extends Enum<?>, ? extends Enum<?>>> chains,
            final Class<? extends Enum<?>> stateType,
            final Class<? extends Enum<?>> eventType,
            final Class<? extends Enum<?>> chainType) {
        super(machineType, transitions, stateType, eventType);
        this.chains = chains;
        this.chainType = chainType;
    }

    public static DefaultCFSMConfiguration of(final Class<? extends StateMachine> machineType, final Class<? extends Enum<?>> stateType, final Class<? extends Enum<?>> eventType, final Class<? extends Enum<?>> chainType) {

        return new DefaultCFSMConfiguration(machineType, new ArrayList<>(), new ArrayList<>(), stateType, eventType, chainType);
    }

    /**
     * Adds a chain to the FSM configuration.
     *
     * @param container Container that has to be added to the configuration
     *
     * @return The instance of this container for a fluent like usage.
     */
    public DefaultCFSMConfiguration andAddChain(@NonNull final ChainContainer<? extends Enum<?>, ? extends Enum<?>, ? extends Enum<?>> container) {

        this.chains.add(container);

        return this;
    }

    /**
     * Adds a list of chains to the FSM configuration.
     *
     * @param containers List of chain that have to be added to the configuration
     *
     * @return The instance of this container for a fluent like usage.
     */
    public DefaultCFSMConfiguration andAddChains(final List<ChainContainer<? extends Enum<?>, ? extends Enum<?>, ? extends Enum<?>>> containers) {

        if (Objects.isNull(containers) || containers.isEmpty()) {
            return this;
        }

        this.chains.addAll(containers);

        return this;
    }

    @Override
    public DefaultCFSMConfiguration andAddTransitions(List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> containers) {
        super.andAddTransitions(containers);
        return this;
    }

    @Override
    public DefaultCFSMConfiguration andAddTransition(TransitionContainer<? extends Enum<?>, ? extends Enum<?>> container) {
        super.andAddTransition(container);
        return this;
    }
}
