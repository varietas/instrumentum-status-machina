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
package io.varietas.instrumentum.status.machina.machines.transition;

import io.varietas.instrumentum.status.machina.BasicStateMachine;
import io.varietas.instrumentum.status.machina.annotations.StateMachineConfiguration;
import io.varietas.instrumentum.status.machina.annotations.Transition;
import io.varietas.instrumentum.status.machina.configuration.FSMConfiguration;
import io.varietas.instrumentum.status.machina.models.ExampleChain;
import io.varietas.instrumentum.status.machina.models.ExampleEvent;
import io.varietas.instrumentum.status.machina.models.ExampleState;
import io.varietas.instrumentum.status.machina.models.TestEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>StateMachineWithoutListener</h2>
 */
@Slf4j
@StateMachineConfiguration(stateType = ExampleState.class, eventType = ExampleEvent.class, chainType = ExampleChain.class)
public class StateMachineNotContrivable extends BasicStateMachine {

    private StateMachineNotContrivable(FSMConfiguration configuration) {
        super(configuration);
    }

    @Transition(from = "DEACTIVATED", on = "PARK", to = "PARKED")
    public void fromDeactivatedToParked(final ExampleState from, final ExampleState to, final ExampleEvent event, final TestEntity context) {
        context.setValue(context.getValue() - 5);
    }
}
