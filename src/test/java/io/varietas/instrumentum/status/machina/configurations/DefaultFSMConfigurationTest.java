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
package io.varietas.instrumentum.status.machina.configurations;

import io.varietas.instrumentum.status.machina.machines.transition.StateMachineWithoutListener;
import io.varietas.instrumentum.status.machina.models.ExampleEvent;
import io.varietas.instrumentum.status.machina.models.ExampleState;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Michael Rhöse
 */
public class DefaultFSMConfigurationTest {

    @Test
    public void testAddTransitionsThrowsExceptionOnEmptyList() {
        final DefaultFSMConfiguration configuration = DefaultFSMConfiguration.of(StateMachineWithoutListener.class, ExampleState.class, ExampleEvent.class);
        Throwable result = Assertions.catchThrowable(() -> configuration.andAddTransitions(Collections.emptyList()));
        Assertions.assertThat(result).isInstanceOf(NullPointerException.class);
        Assertions.assertThat(result.getLocalizedMessage()).isEqualTo("Empty list isn't allowed");
    }
}
