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

import io.varietas.instrumentum.status.machina.StateMachine;
import io.varietas.instrumentum.status.machina.configurations.CFSMConfiguration;
import io.varietas.instrumentum.status.machina.configurations.DefaultCFSMConfiguration;
import io.varietas.instrumentum.status.machina.containers.ChainContainer;
import io.varietas.instrumentum.status.machina.containers.ListenerContainer;
import io.varietas.instrumentum.status.machina.containers.TransitionContainer;
import io.varietas.instrumentum.status.machina.errors.TransitionChainCreationException;
import io.varietas.instrumentum.status.machina.listeners.SimpleChainListener;
import io.varietas.instrumentum.status.machina.listeners.SimpleTransitionListener;
import io.varietas.instrumentum.status.machina.machines.chain.ChainStateMachineNothingAvailable;
import io.varietas.instrumentum.status.machina.machines.chain.ChainStateMachineSimple;
import io.varietas.instrumentum.status.machina.machines.chain.ChainStateMachineWithChainListener;
import io.varietas.instrumentum.status.machina.machines.chain.ChainStateMachineWithSingleTransitionChain;
import io.varietas.instrumentum.status.machina.machines.chain.ChainStateMachineWithoutListener;
import io.varietas.instrumentum.status.machina.models.ExampleChain;
import io.varietas.instrumentum.status.machina.models.ExampleEvent;
import io.varietas.instrumentum.status.machina.models.ExampleState;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Michael Rhöse
 */
@Slf4j
public class SimpleChainStateMachineBuilderTest {

    private SoftAssertions softly;

    @BeforeEach
    public void beforeEach() {
        this.softly = new SoftAssertions();
    }

    @AfterEach
    public void afterEach() {
        softly.assertAll();
        this.softly = null;
    }

    @Test
    public void testExtractConfigurationWithoutTransitionListener() {

        this.assertStateMachineConfiguration(ChainStateMachineWithoutListener.class, false, false, true);
    }

    @Test
    public void testExtractConfigurationWithChainListener() {

        this.assertStateMachineConfiguration(ChainStateMachineWithChainListener.class, false, false, true);
    }

    @Test
    public void testFailingChainCreation() {

        Assertions.assertThatThrownBy(() -> SimpleChainStateMachineBuilder.getBuilder().extractConfiguration(ChainStateMachineSimple.class))
                .isInstanceOf(TransitionChainCreationException.class);
    }

    @Test
    public void testSingleChainAvailable() {
        CFSMConfiguration configuration = SimpleChainStateMachineBuilder.getBuilder().extractConfiguration(ChainStateMachineWithSingleTransitionChain.class).configuration();
        Assertions.assertThat(configuration.getChains()).hasSize(1);
    }

    @Test
    public void testNoAnnotationAvailable() {
        CFSMConfiguration configuration = SimpleChainStateMachineBuilder.getBuilder().extractConfiguration(ChainStateMachineNothingAvailable.class).configuration();
        Assertions.assertThat(configuration.getChains()).isEmpty();
    }

    private void assertStateMachineConfiguration(final Class<? extends StateMachine> stateMachineType, final boolean isAssertMethod, final boolean isAssertListeners, final boolean isAssertChainListener) {

        CFSMConfiguration expectedResult = this.createConfiguration(stateMachineType);
        CFSMConfiguration result = SimpleChainStateMachineBuilder.getBuilder().extractConfiguration(stateMachineType).configuration();

        this.softly.assertThat(expectedResult.getStateType()).isEqualTo(result.getStateType());
        this.softly.assertThat(expectedResult.getEventType()).isEqualTo(result.getEventType());
        this.softly.assertThat(((DefaultCFSMConfiguration) expectedResult).getChainType()).isEqualTo(((DefaultCFSMConfiguration) result).getChainType());

        this.softly.assertThat(expectedResult.getTransitions()).hasSameSizeAs(result.getTransitions());

        for (int index = 0; index < result.getTransitions().size(); index++) {
            TransitionContainer<? extends Enum<?>, ? extends Enum<?>> expContainer = expectedResult.getTransitions().get(index);
            Optional<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> container = result.getTransitions().stream()
                    .filter(res -> res.getFrom().equals(expContainer.getFrom()) && res.getTo().equals(expContainer.getTo()))
                    .findFirst();
            this.softly.assertThat(container).overridingErrorMessage("Transaction container 's' doesn't contained in list", expContainer.getOn().name()).isPresent();
            this.assertTransitionContainer(container.get(), expContainer, isAssertMethod, isAssertListeners);
        }

        if (isAssertChainListener) {
            this.softly.assertThat(((DefaultCFSMConfiguration) expectedResult).getChains()).hasSameSizeAs(((DefaultCFSMConfiguration) result).getChains());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void assertTransitionContainer(final TransitionContainer one, final TransitionContainer other, final boolean isAssertMethod, final boolean isAssertListeners) {

        this.softly.assertThat(one.getFrom()).isEqualTo(other.getFrom());
        this.softly.assertThat(one.getTo()).isEqualTo(other.getTo());
        this.softly.assertThat(one.getOn()).isEqualTo(other.getOn());

        if (isAssertMethod) {
            this.softly.assertThat(one.getCalledMethod()).isEqualTo(other.getCalledMethod());
        }
        if (isAssertListeners) {
            this.softly.assertThat(one.getListeners()).hasSameElementsAs(other.getListeners());
        }
    }

    @SuppressWarnings("unchecked")
    private CFSMConfiguration createConfiguration(final Class<? extends StateMachine> machineType) {

        try {
            final Method action = SimpleChainStateMachineBuilderTest.class.getDeclaredMethod("calledTransitionMethod");

            final ListenerContainer transitionListener = ListenerContainer.of(SimpleTransitionListener.class, true, true);

            final ListenerContainer chainListener = ListenerContainer.of(SimpleChainListener.class, true, true);

            final TransitionContainer singleTransition = TransitionContainer.of(ExampleState.DEACTIVATED, ExampleState.PARKED, ExampleEvent.PARK, action).andAddListener(transitionListener);
            final List<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> transitions = new ArrayList<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>>() {
                {
                    add(TransitionContainer.of(ExampleState.AVAILABLE, ExampleState.REGISTERED, ExampleEvent.REGISTER, action).andAddListener(transitionListener)); // 2
                    add(TransitionContainer.of(ExampleState.REGISTERED, ExampleState.ACTIVATED, ExampleEvent.ACTIVATE, action).andAddListener(transitionListener)); // 3
                    add(TransitionContainer.of(ExampleState.PARKED, ExampleState.ACTIVATED, ExampleEvent.ACTIVATE, action).andAddListener(transitionListener)); // 4
                    add(TransitionContainer.of(ExampleState.REGISTERED, ExampleState.DELETED, ExampleEvent.DELETE, action).andAddListener(transitionListener)); // 0
                    add(TransitionContainer.of(ExampleState.UNREGISTERED, ExampleState.DELETED, ExampleEvent.DELETE, action).andAddListener(transitionListener)); // 1
                    add(TransitionContainer.of(ExampleState.ACTIVATED, ExampleState.DEACTIVATED, ExampleEvent.DEACTIVATE, action).andAddListener(transitionListener)); // 5
                    add(TransitionContainer.of(ExampleState.DEACTIVATED, ExampleState.UNREGISTERED, ExampleEvent.UNREGISTER, action).andAddListener(transitionListener)); // 6
                    add(TransitionContainer.of(ExampleState.PARKED, ExampleState.UNREGISTERED, ExampleEvent.UNREGISTER, action).andAddListener(transitionListener)); // 7
                }
            };

            final List<ChainContainer<? extends Enum<?>, ? extends Enum<?>, ? extends Enum<?>>> chains = new ArrayList<ChainContainer<? extends Enum<?>, ? extends Enum<?>, ? extends Enum<?>>>() {
                {
                    add(ChainContainer.of(ExampleState.AVAILABLE, ExampleState.ACTIVATED, ExampleChain.INSTALLING).andAdd(transitions.get(0)).andAdd(transitions.get(2)).andAdd(chainListener));
                    add(ChainContainer.of(ExampleState.ACTIVATED, ExampleState.PARKED, ExampleChain.PARKING).andAdd(transitions.get(5)).andAdd(singleTransition).andAdd(chainListener));
                    add(ChainContainer.of(ExampleState.ACTIVATED, ExampleState.DELETED, ExampleChain.DELETION).andAdd(transitions.get(5)).andAdd(transitions.get(6)).andAdd(transitions.get(1)).andAdd(chainListener));
                }
            };

            return DefaultCFSMConfiguration.of(machineType, ExampleState.class, ExampleEvent.class, ExampleChain.class)
                    .andAddChains(chains)
                    .andAddTransition(singleTransition)
                    .andAddChain(ChainContainer.of(ExampleState.DELETED, ExampleState.DELETED, ExampleChain.DELETION).andAdd(transitions.get(7)).andAdd(transitions.get(4)).andAdd(chainListener))
                    .andAddTransitions(transitions);

        }
        catch (NoSuchMethodException | SecurityException ex) {
            LOGGER.error(ex.getLocalizedMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * This method is used as the called method for a transition.
     */
    public void calledTransitionMethod() {
        System.out.println("io.varietas.instrumentum.status.machina.builders.impl.SimpleChainStateMachineBuilderTest.calledTransitionMethod()");
    }
}
