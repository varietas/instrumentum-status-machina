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

import io.varietas.instrumentum.status.machina.AbstractStateMachine;
import io.varietas.instrumentum.status.machina.annotations.StateMachineConfiguration;
import io.varietas.instrumentum.status.machina.annotations.Transition;
import io.varietas.instrumentum.status.machina.annotations.TransitionListener;
import io.varietas.instrumentum.status.machina.configuration.FSMConfiguration;
import io.varietas.instrumentum.status.machina.listeners.SimpleTransitionAfterListener;
import io.varietas.instrumentum.status.machina.listeners.SimpleTransitionBeforeListener;
import io.varietas.instrumentum.status.machina.models.Chain;
import io.varietas.instrumentum.status.machina.models.Event;
import io.varietas.instrumentum.status.machina.models.State;
import io.varietas.instrumentum.status.machina.models.TestEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>StateMachineWithoutListener</h2>
 */
@Slf4j
@StateMachineConfiguration(stateType = State.class, eventType = Event.class, chainType = Chain.class)
public class StateMachineWithMultipleListeners extends AbstractStateMachine {

    public StateMachineWithMultipleListeners(FSMConfiguration configuration) {
        super(configuration);
    }

    @TransitionListener(SimpleTransitionBeforeListener.class)
    @TransitionListener(SimpleTransitionAfterListener.class)
    @Transition(from = "ACTIVATED", on = "DEACTIVATE", to = "DEACTIVATED")
    public void fromActivatedToDeactivated(final State from, final State to, final Event event, final TestEntity context) {
        context.setValue(context.getValue() - 2);
    }
}
