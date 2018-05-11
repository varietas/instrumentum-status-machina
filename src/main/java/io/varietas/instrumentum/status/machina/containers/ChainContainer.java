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
package io.varietas.instrumentum.status.machina.containers;

import io.varietas.instrumentum.status.machina.StateMachine;
import io.varietas.instrumentum.status.machina.builders.impl.StateMachineBuilderImpl;
import io.varietas.instrumentum.status.machina.annotations.TransitionChain;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * <h2>ChainContainer</h2>
 * <p>
 * The chain container holds relevant information collected from {@link TransitionChain} annotation on runtime. This includes the
 * <ul>
 * <li>required start state,</li>
 * <li>state after transition,</li>
 * <li>name of the chain and</li>
 * <li>the list of transitions, which are passed.</li>
 * </ul>
 * <p>
 * The transitions are collected while the configuration is created automatically by the {@link StateMachineBuilderImpl}. Its recommended that a configuration is created once per {@link StateMachine}
 * and shared between the instances.
 *
 * @author Michael Rhöse
 * @version 1.0.0, 10/7/2017
 * @param <STATE_TYPE>      Generic type of enumeration which is used to represent the states.
 * @param <TRANSITION_TYPE> Generic type of enumeration which is used to represent the occurred event (Event identifier).
 * @param <CHAIN_TYPE>      Generic type of enumeration which is used to represent a chain event (Chain identifier).
 */
@ToString(exclude = {"from", "to", "chainParts"})
@EqualsAndHashCode(exclude = "chainParts")
@Getter
@AllArgsConstructor
public class ChainContainer<STATE_TYPE extends Enum, TRANSITION_TYPE extends Enum, CHAIN_TYPE extends Enum> {

    private final STATE_TYPE from;

    private final STATE_TYPE to;

    private final CHAIN_TYPE on;

    private final List<TransitionContainer> chainParts;

    private final List<ListenerContainer> listeners;
}
