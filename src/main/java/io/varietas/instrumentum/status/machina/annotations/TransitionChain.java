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
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h2>TransitionChain</h2>
 * <p>
 * A transition chain holds all information required for a automated state change from on to another state.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/7/2017
 */
@Documented
@Repeatable(TransitionChains.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TransitionChain {

    /**
     * Constants as string which stands for the start state.
     *
     * @return Start state.
     */
    String from();

    /**
     * Constants as string which stands for the state after chain.
     *
     * @return End state.
     */
    String to();

    /**
     * Constants as string which stands for the name of the transition chain.
     *
     * @return Transition chain identifier.
     */
    String on();
}
