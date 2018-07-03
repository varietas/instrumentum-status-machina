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
package io.varietas.instrumentum.status.machina.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h2>StateMachineConfiguration</h2>
 * <p>
 * This annotation holds the general configuration of a state machine. This way of configuration is inspired by squirrel-framework FSM.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/7/2017
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface StateMachineConfiguration {

    /**
     * Type of enumeration which is used to represent the states.
     *
     * @return Type of state.
     */
    Class<? extends Enum> stateType();

    /**
     * Type of enumeration which is used to represent the occurred event (Event identifier).
     *
     * @return Type of event.
     */
    Class<? extends Enum> eventType();

    /**
     * Type of enumeration which is used to represent a chain event (Chain identifier).
     *
     * @return Type of chain.
     */
    Class<? extends Enum> chainType();
}
