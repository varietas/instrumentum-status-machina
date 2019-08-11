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

import io.varietas.instrumentum.status.machina.models.ExampleEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Michael Rhöse
 */
public class InvalidTransitionExceptionTest {

    @Test
    public void testGetLocalizedMessageWithoutAdditionalThrowable() {
        InvalidTransitionException instance = new InvalidTransitionException(ExampleEvent.REGISTER, "The transition is not possible");
        String expResult = "Transition 'REGISTER' isn't possible: The transition is not possible.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetLocalizedMessageWithAdditionalThrowable() {
        InvalidTransitionException instance = new InvalidTransitionException(ExampleEvent.REGISTER, "The transition is not possible", new NullPointerException("Any null pointer"));
        String expResult = "Transition 'REGISTER' isn't possible: The transition is not possible. NullPointerException: Any null pointer.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testNullEnumAndAnyMessage() {
        Assertions.assertThatThrownBy(() -> new InvalidTransitionException(null, "")).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testAnyEnumAndNullMessage() {
        Assertions.assertThatThrownBy(() -> new InvalidTransitionException(ExampleEvent.ACTIVATE, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNullEnumAndAnyMessageAndAnyCause() {
        Assertions.assertThatThrownBy(() -> new InvalidTransitionException(null, "", new NullPointerException())).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testAnyEnumAndNullMessageAnyCause() {
        Assertions.assertThatThrownBy(() -> new InvalidTransitionException(ExampleEvent.ACTIVATE, null, new NullPointerException())).isInstanceOf(NullPointerException.class);
    }
}
