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

import io.varietas.instrumentum.status.machina.configuration.FSMConfiguration;
import io.varietas.instrumentum.status.machina.container.TransitionContainer;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * <h2>FSMConfigurationImpl</h2>
 * <p>
 * This class represents a container to use FSM in a dependency injection framework like agrestis imputare. It allows the separate storing of configuration as singleton.
 *
 * @author Michael Rhöse
 * @version 1.0.0, 10/10/2017
 */
@ToString
@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class FSMConfigurationImpl implements FSMConfiguration {

    private final List<TransitionContainer> transitions;

    private final Class<? extends Enum> stateType;

    private final Class<? extends Enum> eventType;
}
