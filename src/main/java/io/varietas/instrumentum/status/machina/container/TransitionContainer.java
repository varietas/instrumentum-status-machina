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
package io.varietas.instrumentum.status.machina.container;

import io.varietas.instrumentum.status.machina.annotation.Transition;
import java.lang.reflect.Method;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * <h2>TransitionContainer</h2>
 *
 * The transition container holds relevant information collected from {@link Transition} annotation on runtime. This includes the
 * <ul>
 * <li>required start state,</li>
 * <li>state after transition,</li>
 * <li>name of the transition and</li>
 * <li>method which is invoked to manipulate the transition target.</li>
 * </ul>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 10/7/2017
 * @param <STATE_TYPE> Generic type of enumeration which is used to represent the states.
 * @param <TRANSITION_TYPE> Generic type of enumeration which is used to represent the occurred event (Event identifier).
 */
@ToString(exclude = {"calledMethod"})
@EqualsAndHashCode(exclude = "calledMethod")
@Getter
@AllArgsConstructor
public class TransitionContainer<STATE_TYPE extends Enum, TRANSITION_TYPE extends Enum> {

    private final STATE_TYPE from;

    private final STATE_TYPE to;

    private final TRANSITION_TYPE on;

    private final Method calledMethod;

    private List<ListenerContainer> listeners;
}
