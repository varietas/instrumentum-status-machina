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
package io.varietas.instrumentum.status.machina.builders.impl;

import io.varietas.instrumentum.status.machina.models.Chain;
import io.varietas.instrumentum.status.machina.listeners.SimpleChainListener;
import io.varietas.instrumentum.status.machina.listeners.SimpleTransitionListener;
import io.varietas.instrumentum.status.machina.models.State;
import io.varietas.instrumentum.status.machina.models.Event;
import io.varietas.instrumentum.status.machina.machines.chain.ChainStateMachineWithChainListener;
import io.varietas.instrumentum.status.machina.machines.transition.StateMachineWithTransitionListener;
import io.varietas.instrumentum.status.machina.machines.chain.ChainStateMachineWithoutListener;
import io.varietas.instrumentum.status.machina.StateMachine;
import io.varietas.instrumentum.status.machina.builders.StateMachineBuilder;
import io.varietas.instrumentum.status.machina.configuration.CFSMConfiguration;
import io.varietas.instrumentum.status.machina.configuration.impl.CFSMConfigurationImpl;
import io.varietas.instrumentum.status.machina.containers.ChainContainer;
import io.varietas.instrumentum.status.machina.containers.ListenerContainer;
import io.varietas.instrumentum.status.machina.containers.TransitionContainer;
import io.varietas.instrumentum.status.machina.error.TransitionChainCreationException;
import io.varietas.instrumentum.status.machina.machines.chain.ChainStateMachineNothingAvailable;
import io.varietas.instrumentum.status.machina.machines.chain.ChainStateMachineSimple;
import io.varietas.instrumentum.status.machina.machines.chain.ChainStateMachineWithSingleTransitionChain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Michael Rhöse
 */
@Slf4j
public class ChainStateMachineBuilderImplTest {

    @Test
    public void testExtractConfigurationWithoutTransitionListener() {

        this.assertStateMachineConfiguration(ChainStateMachineWithoutListener.class, false, false, true);
    }

    @Test
    public void testExtractConfigurationWithTransitionListener() {

        this.assertStateMachineConfiguration(StateMachineWithTransitionListener.class, false, true, false);
    }

    @Test
    public void testExtractConfigurationWithChainListener() {

        this.assertStateMachineConfiguration(ChainStateMachineWithChainListener.class, false, false, true);
    }

    @Test
    public void testFailingChainCreation() {

        Assertions.assertThatThrownBy(() -> new ChainStateMachineBuilderImpl().extractConfiguration(ChainStateMachineSimple.class))
                .isInstanceOf(TransitionChainCreationException.class);
    }

    @Test
    public void testSingleChainAvailable() {
        CFSMConfiguration configuration = new ChainStateMachineBuilderImpl().extractConfiguration(ChainStateMachineWithSingleTransitionChain.class).configuration();
        Assertions.assertThat(configuration.getChains()).hasSize(1);
    }

    @Test
    public void testNoAnnotationAvailable() {
        CFSMConfiguration configuration = new ChainStateMachineBuilderImpl().extractConfiguration(ChainStateMachineNothingAvailable.class).configuration();
        Assertions.assertThat(configuration.getChains()).isEmpty();
    }

    private void assertStateMachineConfiguration(final Class<? extends StateMachine> stateMachineType, final boolean isAssertMethod, final boolean isAssertListeners, final boolean isAssertChainListener) {

        CFSMConfiguration expectedResult = this.createConfiguration(stateMachineType);
        CFSMConfiguration result = new ChainStateMachineBuilderImpl().extractConfiguration(stateMachineType).configuration();

        Assertions.assertThat(expectedResult.getStateType()).isEqualTo(result.getStateType());
        Assertions.assertThat(expectedResult.getEventType()).isEqualTo(result.getEventType());
        Assertions.assertThat(((CFSMConfigurationImpl) expectedResult).getChainType()).isEqualTo(((CFSMConfigurationImpl) result).getChainType());

        Assertions.assertThat(expectedResult.getTransitions()).hasSameSizeAs(result.getTransitions());

        for (int index = 0; index < result.getTransitions().size(); index++) {
            TransitionContainer expContainer = expectedResult.getTransitions().get(index);
            Optional<TransitionContainer> container = result.getTransitions().stream()
                    .filter(res -> res.getFrom().equals(expContainer.getFrom()) && res.getTo().equals(expContainer.getTo()))
                    .findFirst();
            Assertions.assertThat(container).isPresent();
            this.assertTransitionContainer(container.get(), expContainer, isAssertMethod, isAssertListeners);
        }

        if (isAssertChainListener) {
            Assertions.assertThat(((CFSMConfigurationImpl) expectedResult).getChains()).hasSameSizeAs(((CFSMConfigurationImpl) result).getChains());
        }
    }

    private void assertTransitionContainer(final TransitionContainer one, final TransitionContainer other, final boolean isAssertMethod, final boolean isAssertListeners) {

        Assertions.assertThat(one.getFrom()).isEqualTo(other.getFrom());
        LOGGER.info("From equals {} | {}", one.getFrom(), other.getFrom());
        Assertions.assertThat(one.getTo()).isEqualTo(other.getTo());
        LOGGER.info("To equals {} | {}", one.getTo(), other.getTo());
        Assertions.assertThat(one.getOn()).isEqualTo(other.getOn());
        LOGGER.info("On equals {} | {}", one.getOn(), other.getOn());

        if (isAssertMethod) {
            Assertions.assertThat(one.getCalledMethod()).isEqualTo(other.getCalledMethod());
        }
        if (isAssertListeners) {
            Assertions.assertThat(one.getListeners()).hasSameElementsAs(other.getListeners());
        }
    }

    private CFSMConfiguration createConfiguration(final Class<? extends StateMachine> machineType) {
        List<ListenerContainer> transitionListeners = new ArrayList<ListenerContainer>() {
            {
                add(new ListenerContainer(SimpleTransitionListener.class, true, true));
            }
        };
        List<ListenerContainer> chainListeners = new ArrayList<ListenerContainer>() {
            {
                add(new ListenerContainer(SimpleChainListener.class, true, true));
            }
        };

        List<TransitionContainer> transitions = new ArrayList<TransitionContainer>() {
            {
                add(new TransitionContainer(State.AVAILABLE, State.REGISTERED, Event.REGISTER, null, transitionListeners)); // 2
                add(new TransitionContainer(State.REGISTERED, State.ACTIVATED, Event.ACTIVATE, null, transitionListeners)); // 3
                add(new TransitionContainer(State.PARKED, State.ACTIVATED, Event.ACTIVATE, null, transitionListeners)); // 4
                add(new TransitionContainer(State.REGISTERED, State.DELETED, Event.DELETE, null, transitionListeners)); // 0
                add(new TransitionContainer(State.UNREGISTERED, State.DELETED, Event.DELETE, null, transitionListeners)); // 1
                add(new TransitionContainer(State.ACTIVATED, State.DEACTIVATED, Event.DEACTIVATE, null, transitionListeners)); // 5
                add(new TransitionContainer(State.DEACTIVATED, State.UNREGISTERED, Event.UNREGISTER, null, transitionListeners)); // 6
                add(new TransitionContainer(State.PARKED, State.UNREGISTERED, Event.UNREGISTER, null, transitionListeners)); // 7
                add(new TransitionContainer(State.DEACTIVATED, State.PARKED, Event.PARK, null, transitionListeners)); // 8
            }
        };

        List<ChainContainer> chains = new ArrayList<ChainContainer>() {
            {
                add(new ChainContainer(State.AVAILABLE, State.ACTIVATED, Chain.INSTALLING, Arrays.asList(transitions.get(0), transitions.get(2)), chainListeners));
                add(new ChainContainer(State.ACTIVATED, State.PARKED, Chain.PARKING, Arrays.asList(transitions.get(5), transitions.get(8)), chainListeners));
                add(new ChainContainer(State.ACTIVATED, State.DELETED, Chain.DELETION, Arrays.asList(transitions.get(5), transitions.get(6), transitions.get(1)), chainListeners));
                add(new ChainContainer(State.DELETED, State.DELETED, Chain.DELETION, Arrays.asList(transitions.get(7), transitions.get(4)), chainListeners));
            }
        };

        return new CFSMConfigurationImpl(
                machineType,
                transitions,
                chains,
                State.class,
                Event.class,
                Chain.class
        );
    }
}
