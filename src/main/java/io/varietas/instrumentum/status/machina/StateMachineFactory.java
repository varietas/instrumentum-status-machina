/*
 * Copyright 2019 Michael Rhöse.
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

import io.varietas.instrumentum.status.machina.builders.StateMachineBuilder;
import io.varietas.instrumentum.status.machina.builders.impl.ChainStateMachineBuilderImpl;
import io.varietas.instrumentum.status.machina.builders.impl.StateMachineBuilderImpl;
import io.varietas.instrumentum.status.machina.error.MachineCreationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <h2>StateMachineFactory</h2>
 * <p>
 * The state machine factory creates a state machine by its configuration. It is the easiest way to work with status machina.
 *
 * @author Michael Rhöse
 * @version 1.0.1.0, 02/03/2019
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StateMachineFactory {

    public static StateMachine getStateMachine(final Class<? extends StateMachine> type) throws MachineCreationException {
        StateMachineBuilder builder;

        if (type == ChainStateMachine.class) {
            builder = new ChainStateMachineBuilderImpl();
        } else {
            builder = new StateMachineBuilderImpl();
        }

        return builder.extractConfiguration(type).build();
    }
}
