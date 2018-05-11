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

import io.varietas.instrumentum.status.machina.configuration.impl.CFSMConfigurationImpl;
import io.varietas.instrumentum.status.machina.configuration.FSMConfiguration;
import io.varietas.instrumentum.status.machina.container.ChainContainer;
import io.varietas.instrumentum.status.machina.container.ListenerContainer;
import io.varietas.instrumentum.status.machina.container.TransitionContainer;
import io.varietas.instrumentum.status.machina.error.InvalidTransitionException;
import io.varietas.instrumentum.status.machina.error.TransitionInvocationException;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>AbstractChainStateMachine</h2>
 * <p>
 * This class represents an abstract implementation of the {@link ChainStateMachine} interface. The default implementation contains the firing of single transitions and transition chains.
 *
 * @author Michael Rhöse
 * @version 1.0.0, 10/27/2017
 */
@Slf4j
public abstract class AbstractChainStateMachine extends AbstractStateMachine implements ChainStateMachine {

    public AbstractChainStateMachine(FSMConfiguration configuration) {
        super(configuration);
    }

    /**
     * This method searches the container which contains all information required to performing transition operations.
     *
     * @param transitionChain Next transition chain kind.
     * @param startState      Start state of the current transition target for identification of the right chain.
     *
     * @return Expected container for the transition chain, otherwise an empty Optional.
     */
    protected Optional<ChainContainer> findChainContainer(final Enum transitionChain, final Enum startState) {
        return ((CFSMConfigurationImpl) this.configuration).getChains().stream()
                .filter(chain -> chain.getOn().equals(transitionChain))
                .filter(chain -> chain.getFrom().equals(startState))
                .findFirst();
    }

    @Override
    public void fireChain(Enum transitionChain, StatedObject target) throws TransitionInvocationException, InvalidTransitionException {
        final Optional<ChainContainer> chainContainer = this.findChainContainer(transitionChain, target.state());

        if (!chainContainer.isPresent()) {
            throw new InvalidTransitionException(transitionChain, "Couldn't find chain.");
        }

        if (!target.state().equals(chainContainer.get().getFrom())) {
            throw new InvalidTransitionException(transitionChain, "Current state " + target.state().name() + " doesn't match required state " + chainContainer.get().getFrom().name() + ".");
        }

        if (Objects.nonNull(chainContainer.get().getListeners())) {
            chainContainer.get().getListeners().forEach(listener -> this.executeListener((ListenerContainer) listener, "before", chainContainer.get().getOn(), target));
        }

        chainContainer.get().getChainParts().forEach(chainPart -> this.fire((TransitionContainer) chainPart, target));

        if (Objects.nonNull(chainContainer.get().getListeners())) {
            chainContainer.get().getListeners().forEach(listener -> this.executeListener((ListenerContainer) listener, "after", chainContainer.get().getOn(), target));
        }
    }
}
