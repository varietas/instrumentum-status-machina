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

import io.varietas.instrumentum.status.machina.errors.InvalidTransitionChainException;
import io.varietas.instrumentum.status.machina.errors.MachineCreationException;
import io.varietas.instrumentum.status.machina.errors.TransitionInvocationException;
import io.varietas.instrumentum.status.machina.machines.chain.ChainStateMachineWithAfterListener;
import io.varietas.instrumentum.status.machina.machines.chain.ChainStateMachineWithBeforeListener;
import io.varietas.instrumentum.status.machina.machines.chain.ChainStateMachineWithoutListener;
import io.varietas.instrumentum.status.machina.machines.chain.FailingChainStateMachine;
import io.varietas.instrumentum.status.machina.models.ExampleChain;
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
public class ChainStateMachineTest {

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

    public ChainStateMachine getStateMachine(Class<? extends ChainStateMachine> machineClazz) throws MachineCreationException {
        return (ChainStateMachine) StateMachineFactory.getStateMachine(machineClazz);
    }

    @Test
    public void testFireInvalidChainTransitionError() throws Exception {

        ChainStateMachine stateMachine = this.getStateMachine(ChainStateMachineWithoutListener.class);

        TestEntity entity = TestEntity.of(ExampleState.AVAILABLE, 0);

        Assertions.assertThatThrownBy(() -> stateMachine.fireChain(ExampleState.UNREGISTERED, entity))
                .isInstanceOf(TransitionInvocationException.class);
    }

    @Test
    public void testFailMachineBuilding() {
        Assertions.assertThatThrownBy(() -> this.getStateMachine(FailingChainStateMachine.class))
                .isInstanceOf(MachineCreationException.class);
    }

    @Test
    public void testFireChainWithoutListener() throws Exception {

        ChainStateMachine stateMachine = this.getStateMachine(ChainStateMachineWithoutListener.class);

        TestEntity entity = TestEntity.of(ExampleState.AVAILABLE, 0);
        this.assertTransitionChain(stateMachine, ExampleChain.INSTALLING, ExampleState.ACTIVATED, entity, 3);

        entity = TestEntity.of(ExampleState.ACTIVATED, 0);
        this.assertTransitionChain(stateMachine, ExampleChain.PARKING, ExampleState.PARKED, entity, -7);

        entity = TestEntity.of(ExampleState.ACTIVATED, 0);
        this.assertTransitionChain(stateMachine, ExampleChain.DELETION, ExampleState.DELETED, entity, -10);

        entity = TestEntity.of(ExampleState.PARKED, 0);
        this.assertTransitionChain(stateMachine, ExampleChain.DELETION, ExampleState.DELETED, entity, -8);
    }

    @Test
    public void testFireChainWithBeforeListener() throws Exception {
        ChainStateMachine stateMachine = this.getStateMachine(ChainStateMachineWithBeforeListener.class);

        TestEntity entity = TestEntity.of(ExampleState.AVAILABLE, 0);

        this.assertTransitionChain(stateMachine, ExampleChain.INSTALLING, ExampleState.ACTIVATED, entity, 104);
    }

    @Test
    public void testFireChainWithAfterListener() throws Exception {
        ChainStateMachine stateMachine = this.getStateMachine(ChainStateMachineWithAfterListener.class);

        TestEntity entity = TestEntity.of(ExampleState.AVAILABLE, 0);

        this.assertTransitionChain(stateMachine, ExampleChain.INSTALLING, ExampleState.ACTIVATED, entity, -99);
    }

    private void assertTransitionChain(final ChainStateMachine stateMachine, final ExampleChain event, final ExampleState state, final TestEntity entity, final int expectedValue) throws TransitionInvocationException, InvalidTransitionChainException, InvalidTransitionChainException {
        stateMachine.fireChain(event, entity);
        this.softly.assertThat(entity.getValue()).isEqualTo(expectedValue);
        this.softly.assertThat(entity.state()).isEqualTo(state);
    }
}
