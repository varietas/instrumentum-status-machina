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
package io.varietas.instrumentum.status.machina.machines.chain;

import io.varietas.instrumentum.status.machina.AbstractChainStateMachine;
import io.varietas.instrumentum.status.machina.annotations.StateMachineConfiguration;
import io.varietas.instrumentum.status.machina.annotations.Transition;
import io.varietas.instrumentum.status.machina.configuration.FSMConfiguration;
import io.varietas.instrumentum.status.machina.models.Chain;
import io.varietas.instrumentum.status.machina.models.Event;
import io.varietas.instrumentum.status.machina.models.State;
import io.varietas.instrumentum.status.machina.models.TestEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>LifeCycleStateMachine</h2>
 */
@Slf4j
//@TransitionChain(from = "AVAILABLE", on = "INSTALLING", to = "ACTIVATED")
@StateMachineConfiguration(stateType = State.class, eventType = Event.class, chainType = Chain.class)
public class ChainStateMachineNothingAvailable extends AbstractChainStateMachine {

    public ChainStateMachineNothingAvailable(FSMConfiguration configuration) {
        super(configuration);
    }

    @Transition(from = "AVAILABLE", on = "REGISTER", to = "REGISTERED")
    public void fromAvailableToRegistered(final State from, final State to, final Event event, final TestEntity context) {
        context.setValue(context.getValue() + 1);
    }

    @Transition(from = "PARKED", on = "ACTIVATE", to = "ACTIVATED")
    public void fromAnyToActivated(final State from, final State to, final Event event, final TestEntity context) {
        context.setValue(context.getValue() + 2);
    }
}
