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
package io.varietas.instrumentum.status.machina.configurations;

import io.varietas.instrumentum.status.machina.containers.ChainContainer;
import java.util.List;

/**
 * <h2>CFSMConfiguration</h2>
 * <p>
 * This interface must be implemented by a container that stores the configuration for a {@link io.varietas.instrumentum.status.machina.ChainStateMachine}. It contains all information from the {@link FSMConfiguration} and additional chain information.
 *
 * @see FSMConfiguration
 * @see io.varietas.instrumentum.status.machina.ChainStateMachine
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/27/2017
 */
public interface CFSMConfiguration extends FSMConfiguration {

    /**
     * Returns the collected chains of the FSM type.
     *
     * @return Collected chains.
     */
    List<ChainContainer<? extends Enum<?>, ? extends Enum<?>, ? extends Enum<?>>> getChains();

    /**
     * Returns the chain type of the current FSM type.
     *
     * @return Chain type.
     */
    Class<? extends Enum<?>> getChainType();
}
