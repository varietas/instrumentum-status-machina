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

import io.varietas.instrumentum.status.machina.configurations.DefaultCFSMConfiguration;
import io.varietas.instrumentum.status.machina.configurations.FSMConfiguration;
import io.varietas.instrumentum.status.machina.containers.ChainContainer;
import io.varietas.instrumentum.status.machina.errors.InvalidTransitionChainException;
import io.varietas.instrumentum.status.machina.errors.TransitionInvocationException;
import java.util.Objects;
import java.util.Optional;

/**
 * <h2>AbstractChainStateMachine</h2>
 * <p>
 * This class represents an abstract implementation of the {@link ChainStateMachine} interface. The default implementation contains the firing of single transitions and transition chains.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/27/2017
 */
public abstract class BasicChainStateMachine extends BasicStateMachine implements ChainStateMachine {

    public BasicChainStateMachine(FSMConfiguration configuration) {
        super(configuration);
    }

    /**
     * This method searches the container which contains all information required to performing transition operations.
     *
     * @param transitionChain Next transition chain kind.
     * @param startState Start state of the current transition target for identification of the right chain.
     *
     * @return Expected container for the transition chain, otherwise an empty Optional.
     */
    protected Optional<ChainContainer<? extends Enum<?>, ? extends Enum<?>, ? extends Enum<?>>> findChainContainer(final Enum<?> transitionChain, final Enum<?> startState) {
        return ((DefaultCFSMConfiguration) this.configuration).getChains().stream()
                .filter(chain -> chain.getOn().equals(transitionChain))
                .filter(chain -> chain.getFrom().equals(startState))
                .findFirst();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void fireChain(final Enum<?> transitionChain, final Statable<?> target) throws TransitionInvocationException, InvalidTransitionChainException {
        final Optional<ChainContainer<? extends Enum<?>, ? extends Enum<?>, ? extends Enum<?>>> chainContainer = this.findChainContainer(transitionChain, target.state());

        if (!chainContainer.isPresent()) {
            throw new TransitionInvocationException(transitionChain, "Couldn't find chain.");
        }

        if (Objects.nonNull(chainContainer.get().getListeners())) {
            chainContainer.get().getListeners().forEach(listener -> this.executeListener(listener, "before", chainContainer.get().getOn(), target));
        }

        chainContainer.get().getChainParts().forEach(chainPart -> this.fire(chainPart, target));

        if (Objects.nonNull(chainContainer.get().getListeners())) {
            chainContainer.get().getListeners().forEach(listener -> this.executeListener(listener, "after", chainContainer.get().getOn(), target));
        }
    }
}
