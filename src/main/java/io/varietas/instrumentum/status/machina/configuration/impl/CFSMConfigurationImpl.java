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
package io.varietas.instrumentum.status.machina.configuration.impl;

import io.varietas.instrumentum.status.machina.StateMachine;
import io.varietas.instrumentum.status.machina.configuration.CFSMConfiguration;
import io.varietas.instrumentum.status.machina.containers.ChainContainer;
import io.varietas.instrumentum.status.machina.containers.TransitionContainer;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * <h2>CFSMConfigurationImpl</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 10/27/2017
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
public class CFSMConfigurationImpl extends FSMConfigurationImpl implements CFSMConfiguration {

    private final List<ChainContainer> chains;

    private final Class<? extends Enum> chainType;

    public CFSMConfigurationImpl(
            final Class<? extends StateMachine> machineType,
            final List<TransitionContainer> transitions,
            final List<ChainContainer> chains,
            final Class<? extends Enum> stateType,
            final Class<? extends Enum> eventType,
            final Class<? extends Enum> chainType) {
        super(machineType, transitions, stateType, eventType);
        this.chains = chains;
        this.chainType = chainType;
    }
}