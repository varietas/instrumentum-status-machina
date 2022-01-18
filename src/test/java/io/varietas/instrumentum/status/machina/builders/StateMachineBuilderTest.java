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
package io.varietas.instrumentum.status.machina.builders;

import io.varietas.instrumentum.status.machina.StateMachine;
import io.varietas.instrumentum.status.machina.configurations.DefaultFSMConfiguration;
import io.varietas.instrumentum.status.machina.configurations.FSMConfiguration;
import io.varietas.instrumentum.status.machina.containers.TransitionContainer;
import io.varietas.instrumentum.status.machina.errors.MachineCreationException;
import io.varietas.instrumentum.status.machina.machines.transition.StateMachineNotContrivable;
import io.varietas.instrumentum.status.machina.machines.transition.StateMachineWithMultipleListeners;
import io.varietas.instrumentum.status.machina.machines.transition.StateMachineWithoutListener;
import io.varietas.instrumentum.status.machina.models.ExampleEvent;
import io.varietas.instrumentum.status.machina.models.ExampleState;
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
public class StateMachineBuilderTest {

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
    public void testExtractConfiguration() {
        this.assertStateMachineConfiguration(StateMachineWithoutListener.class, false, false);
    }

    @Test
    @SuppressWarnings("null")
    public void testNullStateMachineType() {
        Assertions.assertThatThrownBy(() -> SimpleStateMachineBuilder.getBuilder().extractConfiguration(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testMachineCreationException() {
        Assertions.assertThatThrownBy(() -> SimpleStateMachineBuilder.getBuilder().extractConfiguration(StateMachineNotContrivable.class).build()).isInstanceOf(MachineCreationException.class);
    }

    @Test
    public void testCreationOfValideMachine() {
        Assertions.assertThat(SimpleStateMachineBuilder.getBuilder().extractConfiguration(StateMachineWithoutListener.class)).isNotNull();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCreationOfValideMachineConfiguration() {
        StateMachineBuilder<FSMConfiguration> builder = SimpleStateMachineBuilder.getBuilder().extractConfiguration(StateMachineWithoutListener.class);
        Assertions.assertThat(builder.configuration()).isNotNull();
    }

    @Test
    public void testSetConfiguration() throws MachineCreationException {
        FSMConfiguration configuration = SimpleStateMachineBuilder.getBuilder().extractConfiguration(StateMachineWithoutListener.class).configuration();
        Assertions.assertThat(SimpleStateMachineBuilder.getBuilder().configuration(configuration).build()).isNotNull();
    }

    @Test
    public void testMultipleListenersAvailable() throws MachineCreationException {
        FSMConfiguration configuration = SimpleStateMachineBuilder.getBuilder().extractConfiguration(StateMachineWithMultipleListeners.class).configuration();
        this.softly.assertThat(configuration.getTransitions()).hasSize(1);

        this.softly.assertThat(configuration.getTransitions().get(0).getListeners()).hasSize(2);
    }

    private void assertStateMachineConfiguration(final Class<? extends StateMachine> stateMachineType, final boolean isAssertMethod, final boolean isAssertListeners) {

        FSMConfiguration expextedResult = this.createConfiguration(stateMachineType);

        SimpleStateMachineBuilder instance = SimpleStateMachineBuilder.getBuilder();
        FSMConfiguration result = instance.extractConfiguration(stateMachineType).configuration();

        this.softly.assertThat(expextedResult.getStateType()).isEqualTo(result.getStateType());
        this.softly.assertThat(expextedResult.getEventType()).isEqualTo(result.getEventType());

        this.softly.assertThat(expextedResult.getTransitions()).hasSameSizeAs(result.getTransitions());

        for (int index = 0; index < result.getTransitions().size(); index++) {
            TransitionContainer<? extends Enum<?>, ? extends Enum<?>> expContainer = expextedResult.getTransitions().get(index);
            Optional<TransitionContainer<? extends Enum<?>, ? extends Enum<?>>> container = result.getTransitions().stream()
                    .filter(res -> res.getFrom().equals(expContainer.getFrom()) && res.getTo().equals(expContainer.getTo()))
                    .findFirst();
            this.softly.assertThat(container).isPresent();
            this.assertTransitionContainer(container.get(), expContainer, isAssertMethod, isAssertListeners);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
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

    private FSMConfiguration createConfiguration(final Class<? extends StateMachine> machineType) {
        try {
            return DefaultFSMConfiguration.of(machineType, ExampleState.class, ExampleEvent.class)
                    .andAddTransition(TransitionContainer.of(ExampleState.AVAILABLE, ExampleState.REGISTERED, ExampleEvent.REGISTER, StateMachineBuilderTest.class.getDeclaredMethod("calledTransitionMethod"))) // 2
                    .andAddTransition(TransitionContainer.of(ExampleState.REGISTERED, ExampleState.ACTIVATED, ExampleEvent.ACTIVATE, StateMachineBuilderTest.class.getDeclaredMethod("calledTransitionMethod"))) // 3
                    .andAddTransition(TransitionContainer.of(ExampleState.PARKED, ExampleState.ACTIVATED, ExampleEvent.ACTIVATE, StateMachineBuilderTest.class.getDeclaredMethod("calledTransitionMethod"))) // 4
                    .andAddTransition(TransitionContainer.of(ExampleState.REGISTERED, ExampleState.DELETED, ExampleEvent.DELETE, StateMachineBuilderTest.class.getDeclaredMethod("calledTransitionMethod"))) // 0
                    .andAddTransition(TransitionContainer.of(ExampleState.UNREGISTERED, ExampleState.DELETED, ExampleEvent.DELETE, StateMachineBuilderTest.class.getDeclaredMethod("calledTransitionMethod"))) // 1
                    .andAddTransition(TransitionContainer.of(ExampleState.ACTIVATED, ExampleState.DEACTIVATED, ExampleEvent.DEACTIVATE, StateMachineBuilderTest.class.getDeclaredMethod("calledTransitionMethod"))) // 5
                    .andAddTransition(TransitionContainer.of(ExampleState.DEACTIVATED, ExampleState.UNREGISTERED, ExampleEvent.UNREGISTER, StateMachineBuilderTest.class.getDeclaredMethod("calledTransitionMethod"))) // 6
                    .andAddTransition(TransitionContainer.of(ExampleState.PARKED, ExampleState.UNREGISTERED, ExampleEvent.UNREGISTER, StateMachineBuilderTest.class.getDeclaredMethod("calledTransitionMethod"))) // 7
                    .andAddTransition(TransitionContainer.of(ExampleState.DEACTIVATED, ExampleState.PARKED, ExampleEvent.PARK, StateMachineBuilderTest.class.getDeclaredMethod("calledTransitionMethod"))); // 8
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
        LOGGER.info("io.varietas.instrumentum.status.machina.builders.impl.SimpleChainStateMachineBuilderTest.calledTransitionMethod()");
    }
}
