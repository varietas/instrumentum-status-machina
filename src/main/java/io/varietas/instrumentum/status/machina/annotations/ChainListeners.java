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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h2>ChainListeners</h2>
 * <p>
 * The chain listeners allow the automated run of additional logic for a chain.
 *
 * @see io.varietas.instrumentum.status.machina.annotations.ChainListener
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/27/2017
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ChainListeners {

    /**
     * Available {@link ChainListener} annotations.
     *
     * @return Transition chains listeners.
     */
    ChainListener[] value();
}
