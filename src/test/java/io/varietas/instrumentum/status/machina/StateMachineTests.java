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

import io.varietas.instrumentum.status.machina.errors.InvalidTransitionException;
import io.varietas.instrumentum.status.machina.errors.MachineCreationException;
import io.varietas.instrumentum.status.machina.machines.transition.FailingStateMachine;
import io.varietas.instrumentum.status.machina.machines.transition.StateMachineWithTransitionAfterListener;
import io.varietas.instrumentum.status.machina.machines.transition.StateMachineWithTransitionBeforeListener;
import io.varietas.instrumentum.status.machina.machines.transition.StateMachineWithoutListener;
import io.varietas.instrumentum.status.machina.models.ExampleEvent;
import io.varietas.instrumentum.status.machina.models.ExampleState;
import io.varietas.instrumentum.status.machina.models.TestEntity;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Michael Rhöse
 */
public class StateMachineTests {

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

    public StateMachine getStateMachine(Class<? extends StateMachine> machineClazz) throws MachineCreationException {
        return StateMachineFactory.getStateMachine(machineClazz);
    }

    @Test
    public void testFireWithoutListenerRegisterToDelete() throws Exception {

        StateMachine stateMachine = this.getStateMachine(StateMachineWithoutListener.class);

        TestEntity entity = TestEntity.of(ExampleState.AVAILABLE, 0);

        this.assertTransition(stateMachine, ExampleEvent.REGISTER, ExampleState.REGISTERED, entity, 1);
        this.assertTransition(stateMachine, ExampleEvent.ACTIVATE, ExampleState.ACTIVATED, entity, 3);
        this.assertTransition(stateMachine, ExampleEvent.DEACTIVATE, ExampleState.DEACTIVATED, entity, 1);
        this.assertTransition(stateMachine, ExampleEvent.UNREGISTER, ExampleState.UNREGISTERED, entity, 0);
        this.assertTransition(stateMachine, ExampleEvent.DELETE, ExampleState.DELETED, entity, -7);
    }

    @Test
    public void testFireInvalidTransitionError() throws Exception {

        StateMachine stateMachine = this.getStateMachine(StateMachineWithoutListener.class);

        TestEntity entity = TestEntity.of(ExampleState.AVAILABLE, 0);

        Assertions.assertThatThrownBy(() -> stateMachine.fire(ExampleEvent.ACTIVATE, entity)).isInstanceOf(InvalidTransitionException.class);
    }

    @Test
    public void testFailMachineBuilding() {
        Assertions.assertThatThrownBy(() -> this.getStateMachine(FailingStateMachine.class))
                .isInstanceOf(MachineCreationException.class);
    }

    @Test
    public void testFireWithoutListenerRegisterToPark() throws Exception {

        StateMachine stateMachine = this.getStateMachine(StateMachineWithoutListener.class);

        TestEntity entity = TestEntity.of(ExampleState.AVAILABLE, 0);

        this.assertTransition(stateMachine, ExampleEvent.REGISTER, ExampleState.REGISTERED, entity, 1);
        this.assertTransition(stateMachine, ExampleEvent.ACTIVATE, ExampleState.ACTIVATED, entity, 3);
        this.assertTransition(stateMachine, ExampleEvent.DEACTIVATE, ExampleState.DEACTIVATED, entity, 1);
        this.assertTransition(stateMachine, ExampleEvent.PARK, ExampleState.PARKED, entity, -4);
    }

    @Test
    public void testFireWithBeforeListener() throws Exception {
        StateMachine stateMachine = this.getStateMachine(StateMachineWithTransitionBeforeListener.class);

        TestEntity entity = TestEntity.of(ExampleState.AVAILABLE, 0);

        this.assertTransition(stateMachine, ExampleEvent.REGISTER, ExampleState.REGISTERED, entity, 81);
        this.assertTransition(stateMachine, ExampleEvent.ACTIVATE, ExampleState.ACTIVATED, entity, 163);
        this.assertTransition(stateMachine, ExampleEvent.DEACTIVATE, ExampleState.DEACTIVATED, entity, 241);
        this.assertTransition(stateMachine, ExampleEvent.UNREGISTER, ExampleState.UNREGISTERED, entity, 320);
        this.assertTransition(stateMachine, ExampleEvent.DELETE, ExampleState.DELETED, entity, 393);
    }

    @Test
    public void testFireWithAfterListener() throws Exception {
        StateMachine stateMachine = this.getStateMachine(StateMachineWithTransitionAfterListener.class);

        TestEntity entity = TestEntity.of(ExampleState.AVAILABLE, 0);

        this.assertTransition(stateMachine, ExampleEvent.REGISTER, ExampleState.REGISTERED, entity, -79);
        this.assertTransition(stateMachine, ExampleEvent.ACTIVATE, ExampleState.ACTIVATED, entity, -157);
        this.assertTransition(stateMachine, ExampleEvent.DEACTIVATE, ExampleState.DEACTIVATED, entity, -239);
        this.assertTransition(stateMachine, ExampleEvent.UNREGISTER, ExampleState.UNREGISTERED, entity, -320);
        this.assertTransition(stateMachine, ExampleEvent.DELETE, ExampleState.DELETED, entity, -407);
    }

    private void assertTransition(final StateMachine stateMachine, final ExampleEvent event, final ExampleState state, final TestEntity entity, final int expectedValue) {
        stateMachine.fire(event, entity);
        this.softly.assertThat(entity.getValue()).isEqualTo(expectedValue);
        this.softly.assertThat(entity.state()).isEqualTo(state);
    }

    @Test
    public void testForInvalidTransition() throws MachineCreationException {
        StateMachine stateMachine = this.getStateMachine(StateMachineWithTransitionAfterListener.class);

        TestEntity entity = TestEntity.of(ExampleState.AVAILABLE, 0);
        Assertions
                .assertThatThrownBy(() -> stateMachine.fire(ExampleEvent.ACTIVATE, entity))
                .isInstanceOf(InvalidTransitionException.class)
                .hasMessage("State of target 'AVAILABLE' doesn't match required state for tarnsition 'ACTIVATE'.");
    }
}
