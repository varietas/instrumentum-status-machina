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
package io.varietas.instrumentum.status.machina.containers;

import io.varietas.instrumentum.status.machina.errors.UnexpectedArgumentException;
import io.varietas.instrumentum.status.machina.models.ExampleChain;
import io.varietas.instrumentum.status.machina.models.ExampleState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <h2>ChainContainerTest</h2>
 * <p>
 * {description}
 *
 * @author Michael Rhöse
 * @version 1.0.1.0, 08/11/2019
 */
public class ChainContainerTest {

    @Test
    @SuppressWarnings("rawtypes")
    public void testAddThrowsExceptionOnUnsupportedType() {
        final ChainContainer container = ChainContainer.of(ExampleState.ACTIVATED, ExampleState.DEACTIVATED, ExampleChain.DELETION);
        Throwable result = Assertions.catchThrowable(() -> container.andAdd(ExampleChain.DELETION));
        Assertions.assertThat(result).isInstanceOf(UnexpectedArgumentException.class);
        Assertions.assertThat(result.getLocalizedMessage()).isEqualTo("Object of type 'io.varietas.instrumentum.status.machina.models.ExampleChain' isn't allowed': Given object is not instance of io.varietas.instrumentum.status.machina.containers.TransitionContainer or io.varietas.instrumentum.status.machina.containers.ListenerContainer.");
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void testAddAllNullResultsInNoError() {
        final ChainContainer container = ChainContainer.of(ExampleState.ACTIVATED, ExampleState.DEACTIVATED, ExampleChain.DELETION);
        container.andAddAll(null);

        Assertions.assertThat(container.getChainParts()).isEmpty();
        Assertions.assertThat(container.getListeners()).isEmpty();
    }
}
