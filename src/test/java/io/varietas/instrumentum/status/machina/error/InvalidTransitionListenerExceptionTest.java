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
package io.varietas.instrumentum.status.machina.error;

import io.varietas.instrumentum.status.machina.listeners.SimpleChainListener;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Michael Rhöse
 */
public class InvalidTransitionListenerExceptionTest {

    public InvalidTransitionListenerExceptionTest() {
    }

    @Test
    public void testGetLocalizedMessage() {
        InvalidTransitionListenerException instance = new InvalidTransitionListenerException(SimpleChainListener.class, "There was an error while performing the listener.");
        String expResult = "Transition listener [io.varietas.instrumentum.status.machina.listeners.SimpleChainListener] not executable. There was an error while performing the listener.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetLocalizedMessageWithAdditionalThrowable() {
        InvalidTransitionListenerException instance = new InvalidTransitionListenerException(SimpleChainListener.class, "There was an error while performing the listener.", new NullPointerException("Any null pointer"));
        String expResult = "Transition listener [io.varietas.instrumentum.status.machina.listeners.SimpleChainListener] not executable. There was an error while performing the listener. NullPointerException: Any null pointer.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testNullListenerTypeAndAnyMessage() {
        Assertions.assertThatThrownBy(() -> new InvalidTransitionListenerException(null, "")).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testAnyListenerTypeAndNullMessage() {
        Assertions.assertThatThrownBy(() -> new InvalidTransitionListenerException(SimpleChainListener.class, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNullListenerTypeAndAnyMessageAndAnyCause() {
        Assertions.assertThatThrownBy(() -> new InvalidTransitionListenerException(null, "", new NullPointerException())).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testAnyListenerTypeAndNullMessageAnyCause() {
        Assertions.assertThatThrownBy(() -> new InvalidTransitionListenerException(SimpleChainListener.class, null, new NullPointerException())).isInstanceOf(NullPointerException.class);
    }
}
