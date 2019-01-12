/*
 * Copyright 2018 Michael Rhöse.
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

import io.varietas.instrumentum.status.machina.models.Event;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Michael Rhöse
 */
public class TransitionInvocationExceptionTest {

    @Test
    public void testGetLocalizedMessage() {
        TransitionInvocationException instance = new TransitionInvocationException(Event.ACTIVATE, "activationMethod");
        String expResult = "Couldn't invoke method 'activationMethod' for transition 'ACTIVATE'.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetLocalizedMessageWithAdditionalMessage() {
        TransitionInvocationException instance = new TransitionInvocationException(Event.ACTIVATE, "activationMethod", "Any message");
        String expResult = "Couldn't invoke method 'activationMethod' for transition 'ACTIVATE': Any message.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetLocalizedMessageAdditionalMessageAndCause() {
        TransitionInvocationException instance = new TransitionInvocationException(Event.ACTIVATE, "activationMethod", "Any message", new NullPointerException("Any null pointer"));
        String expResult = "Couldn't invoke method 'activationMethod' for transition 'ACTIVATE': Any message. NullPointerException: Any null pointer.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetLocalizedMessageAdditionalCause() {
        TransitionInvocationException instance = new TransitionInvocationException(Event.ACTIVATE, "activationMethod", new NullPointerException("Any null pointer"));
        String expResult = "Couldn't invoke method 'activationMethod' for transition 'ACTIVATE'. NullPointerException: Any null pointer.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testNullTransition() {
        Assertions.assertThatThrownBy(() -> new TransitionInvocationException(null, "activationMethod")).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNullMethod() {
        Assertions.assertThatThrownBy(() -> new TransitionInvocationException(Event.ACTIVATE, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNullTransitionWithAdditionalMessage() {
        Assertions.assertThatThrownBy(() -> new TransitionInvocationException(null, "activationMethod", "")).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNullMethodWithAdditionalMessage() {
        Assertions.assertThatThrownBy(() -> new TransitionInvocationException(Event.ACTIVATE, null, "")).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNullTransitionWithAdditionalMessageAndCause() {
        Assertions.assertThatThrownBy(() -> new TransitionInvocationException(null, "activationMethod", "", new NullPointerException())).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNullMethodWithAdditionalMessageAndCause() {
        Assertions.assertThatThrownBy(() -> new TransitionInvocationException(Event.ACTIVATE, null, "", new NullPointerException())).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNullTransitionWithAdditionalCause() {
        Assertions.assertThatThrownBy(() -> new TransitionInvocationException(null, "activationMethod", new NullPointerException())).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNullMethodWithAdditionalCause() {
        Assertions.assertThatThrownBy(() -> new TransitionInvocationException(Event.ACTIVATE, null, new NullPointerException())).isInstanceOf(NullPointerException.class);
    }
}
