/*
 * Copyright 2018 micha.
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
package io.varietas.instrumentum.status.machina.error;

import io.varietas.instrumentum.status.machina.models.ExampleChain;
import io.varietas.instrumentum.status.machina.models.ExampleState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Michael Rhöse
 */
public class TransitionChainCreationExceptionTest {

    @Test
    public void testGetLocalizedMessage() {
        TransitionChainCreationException instance = new TransitionChainCreationException(true, ExampleState.ACTIVATED.name(), ExampleState.AVAILABLE.name(), ExampleChain.INSTALLING.name());
        String expResult = "Couldn't create chain 'INSTALLING'. There is now transition available from 'ACTIVATED' to 'AVAILABLE'.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetLocalizedMessageReverseDirection() {
        TransitionChainCreationException instance = new TransitionChainCreationException(false, ExampleState.ACTIVATED.name(), ExampleState.AVAILABLE.name(), ExampleChain.INSTALLING.name());
        String expResult = "Couldn't create chain 'INSTALLING'. There is now transition available from 'AVAILABLE' to 'ACTIVATED'.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetLocalizedMessageWithAdditionalMessage() {
        TransitionChainCreationException instance = new TransitionChainCreationException(true, ExampleState.ACTIVATED.name(), ExampleState.AVAILABLE.name(), ExampleChain.INSTALLING.name(), "Any message");
        String expResult = "Couldn't create chain 'INSTALLING'. There is now transition available from 'ACTIVATED' to 'AVAILABLE': Any message.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetLocalizedMessageAdditionalMessageAndCause() {
        TransitionChainCreationException instance = new TransitionChainCreationException(true, ExampleState.ACTIVATED.name(), ExampleState.AVAILABLE.name(), ExampleChain.INSTALLING.name(), "Any message", new NullPointerException("Any null pointer"));
        String expResult = "Couldn't create chain 'INSTALLING'. There is now transition available from 'ACTIVATED' to 'AVAILABLE': Any message. NullPointerException: Any null pointer.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetLocalizedMessageAdditionalCause() {
        TransitionChainCreationException instance = new TransitionChainCreationException(true, ExampleState.ACTIVATED.name(), ExampleState.AVAILABLE.name(), ExampleChain.INSTALLING.name(), new NullPointerException("Any null pointer"));
        String expResult = "Couldn't create chain 'INSTALLING'. There is now transition available from 'ACTIVATED' to 'AVAILABLE'. NullPointerException: Any null pointer.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testNUllFrom() {
        Assertions.assertThatThrownBy(() -> new TransitionChainCreationException(true, null, ExampleState.AVAILABLE.name(), ExampleChain.INSTALLING.name())).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNUllTo() {
        Assertions.assertThatThrownBy(() -> new TransitionChainCreationException(true, ExampleState.AVAILABLE.name(), null, ExampleChain.INSTALLING.name())).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNUllChain() {
        Assertions.assertThatThrownBy(() -> new TransitionChainCreationException(true, ExampleState.AVAILABLE.name(), ExampleState.AVAILABLE.name(), null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNUllFromAndAddionalMessage() {
        Assertions.assertThatThrownBy(() -> new TransitionChainCreationException(true, null, ExampleState.AVAILABLE.name(), ExampleChain.INSTALLING.name(), "")).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNUllToAndAddionalMessage() {
        Assertions.assertThatThrownBy(() -> new TransitionChainCreationException(true, ExampleState.AVAILABLE.name(), null, ExampleChain.INSTALLING.name(), "")).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNUllChainAndAddionalMessage() {
        Assertions.assertThatThrownBy(() -> new TransitionChainCreationException(true, ExampleState.AVAILABLE.name(), ExampleState.AVAILABLE.name(), null, "")).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNUllFromAndAddionalMessageAndCause() {
        Assertions.assertThatThrownBy(() -> new TransitionChainCreationException(true, null, ExampleState.AVAILABLE.name(), ExampleChain.INSTALLING.name(), "", new NullPointerException())).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNUllToAndAddionalMessageAndCause() {
        Assertions.assertThatThrownBy(() -> new TransitionChainCreationException(true, ExampleState.AVAILABLE.name(), null, ExampleChain.INSTALLING.name(), "", new NullPointerException())).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNUllChainAndAddionalMessageAndCause() {
        Assertions.assertThatThrownBy(() -> new TransitionChainCreationException(true, ExampleState.AVAILABLE.name(), ExampleState.AVAILABLE.name(), null, "", new NullPointerException())).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNUllFromAndAddionalCause() {
        Assertions.assertThatThrownBy(() -> new TransitionChainCreationException(true, null, ExampleState.AVAILABLE.name(), ExampleChain.INSTALLING.name(), new NullPointerException())).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNUllToAndAddionalCause() {
        Assertions.assertThatThrownBy(() -> new TransitionChainCreationException(true, ExampleState.AVAILABLE.name(), null, ExampleChain.INSTALLING.name(), new NullPointerException())).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNUllChainAndAddionalCause() {
        Assertions.assertThatThrownBy(() -> new TransitionChainCreationException(true, ExampleState.AVAILABLE.name(), ExampleState.AVAILABLE.name(), null, new NullPointerException())).isInstanceOf(NullPointerException.class);
    }

}
