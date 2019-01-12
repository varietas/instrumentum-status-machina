/*
 * Copyright 2018 Michael Rhöse.
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

import io.varietas.instrumentum.status.machina.StateMachine;
import io.varietas.instrumentum.status.machina.builders.StateMachineBuilder;
import io.varietas.instrumentum.status.machina.configuration.FSMConfiguration;
import io.varietas.instrumentum.status.machina.configuration.impl.FSMConfigurationImpl;
import io.varietas.instrumentum.status.machina.containers.TransitionContainer;
import io.varietas.instrumentum.status.machina.error.MachineCreationException;
import io.varietas.instrumentum.status.machina.machines.transition.StateMachineNotContrivable;
import io.varietas.instrumentum.status.machina.machines.transition.StateMachineWithMultipleListeners;
import io.varietas.instrumentum.status.machina.machines.transition.StateMachineWithoutListener;
import io.varietas.instrumentum.status.machina.models.Event;
import io.varietas.instrumentum.status.machina.models.State;
import java.util.ArrayList;
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
public class StateMachineBuilderImplTest {

    @Test
    public void testExtractConfiguration() {
        this.assertStateMachineConfiguration(StateMachineWithoutListener.class, false, false);
    }

    @Test
    public void testNullStateMachineType() {
        Assertions.assertThatThrownBy(() -> new StateMachineBuilderImpl().extractConfiguration(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testMachineCreationException() {
        Assertions.assertThatThrownBy(() -> new StateMachineBuilderImpl().extractConfiguration(StateMachineNotContrivable.class).build()).isInstanceOf(MachineCreationException.class);
    }

    @Test
    public void testCreationOfValideMachine() {
        Assertions.assertThat(new StateMachineBuilderImpl().extractConfiguration(StateMachineWithoutListener.class)).isNotNull();
    }

    @Test
    public void testCreationOfValideMachineConfiguration() {
        StateMachineBuilder<FSMConfiguration> builder = new StateMachineBuilderImpl().extractConfiguration(StateMachineWithoutListener.class);
        Assertions.assertThat(builder.configuration()).isNotNull();
    }

    @Test
    public void testSetConfiguration() throws MachineCreationException {
        FSMConfiguration configuration = new StateMachineBuilderImpl().extractConfiguration(StateMachineWithoutListener.class).configuration();
        Assertions.assertThat(new StateMachineBuilderImpl().configuration(configuration).build()).isNotNull();
    }

    @Test
    public void testMultipleListenersAvailable() throws MachineCreationException {
        FSMConfiguration configuration = new StateMachineBuilderImpl().extractConfiguration(StateMachineWithMultipleListeners.class).configuration();
        Assertions.assertThat(configuration.getTransitions()).hasSize(1);

        Assertions.assertThat(configuration.getTransitions().get(0).getListeners()).hasSize(2);
    }

    private void assertStateMachineConfiguration(final Class<? extends StateMachine> stateMachineType, final boolean isAssertMethod, final boolean isAssertListeners) {

        FSMConfiguration expextedResult = this.createConfiguration(stateMachineType);

        StateMachineBuilderImpl instance = new StateMachineBuilderImpl();
        FSMConfiguration result = instance.extractConfiguration(stateMachineType).configuration();

        Assertions.assertThat(expextedResult.getStateType()).isEqualTo(result.getStateType());
        Assertions.assertThat(expextedResult.getEventType()).isEqualTo(result.getEventType());

        Assertions.assertThat(expextedResult.getTransitions()).hasSameSizeAs(result.getTransitions());

        for (int index = 0; index < result.getTransitions().size(); index++) {
            TransitionContainer expContainer = expextedResult.getTransitions().get(index);
            Optional<TransitionContainer> container = result.getTransitions().stream()
                    .filter(res -> res.getFrom().equals(expContainer.getFrom()) && res.getTo().equals(expContainer.getTo()))
                    .findFirst();
            Assertions.assertThat(container).isPresent();
            this.assertTransitionContainer(container.get(), expContainer, isAssertMethod, isAssertListeners);
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

    private FSMConfiguration createConfiguration(final Class<? extends StateMachine> machineType) {
        List<TransitionContainer> transitions = new ArrayList<TransitionContainer>() {
            {
                add(new TransitionContainer(State.AVAILABLE, State.REGISTERED, Event.REGISTER, null, null)); // 2
                add(new TransitionContainer(State.REGISTERED, State.ACTIVATED, Event.ACTIVATE, null, null)); // 3
                add(new TransitionContainer(State.PARKED, State.ACTIVATED, Event.ACTIVATE, null, null)); // 4
                add(new TransitionContainer(State.REGISTERED, State.DELETED, Event.DELETE, null, null)); // 0
                add(new TransitionContainer(State.UNREGISTERED, State.DELETED, Event.DELETE, null, null)); // 1
                add(new TransitionContainer(State.ACTIVATED, State.DEACTIVATED, Event.DEACTIVATE, null, null)); // 5
                add(new TransitionContainer(State.DEACTIVATED, State.UNREGISTERED, Event.UNREGISTER, null, null)); // 6
                add(new TransitionContainer(State.PARKED, State.UNREGISTERED, Event.UNREGISTER, null, null)); // 7
                add(new TransitionContainer(State.DEACTIVATED, State.PARKED, Event.PARK, null, null)); // 8
            }
        };

        return new FSMConfigurationImpl(
                machineType,
                transitions,
                State.class,
                Event.class
        );
    }
}
