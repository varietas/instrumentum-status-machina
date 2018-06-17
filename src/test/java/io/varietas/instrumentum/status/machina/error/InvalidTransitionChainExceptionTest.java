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

import io.varietas.instrumentum.status.machina.models.Chain;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 * @author Michael Rhöse
 */
@RunWith(JUnit4.class)
public class InvalidTransitionChainExceptionTest {

    @Test
    public void testGetLocalizedMessage() {
        InvalidTransitionChainException instance = new InvalidTransitionChainException(Chain.INSTALLING);
        String expResult = "Chain 'INSTALLING' isn't possible'.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetLocalizedMessageWithAdditionalMessage() {
        InvalidTransitionChainException instance = new InvalidTransitionChainException(Chain.INSTALLING, "Any message");
        String expResult = "Chain 'INSTALLING' isn't possible': Any message.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetLocalizedMessageWithAdditionalMessageAndCause() {
        InvalidTransitionChainException instance = new InvalidTransitionChainException(Chain.INSTALLING, "Any message", new NullPointerException("Any null pointer"));
        String expResult = "Chain 'INSTALLING' isn't possible': Any message. NullPointerException: Any null pointer.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetLocalizedMessageWithAdditionalCause() {
        InvalidTransitionChainException instance = new InvalidTransitionChainException(Chain.INSTALLING, new NullPointerException("Any null pointer"));
        String expResult = "Chain 'INSTALLING' isn't possible'. NullPointerException: Any null pointer.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testNUllChainEnum() {
        Assertions.assertThatThrownBy(() -> new InvalidTransitionChainException(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNUllChainEnumAndAnyMessage() {
        Assertions.assertThatThrownBy(() -> new InvalidTransitionChainException(null, "")).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNUllChainEnumAndAnyMessageAndAnyCause() {
        Assertions.assertThatThrownBy(() -> new InvalidTransitionChainException(null, "", new NullPointerException())).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNUllChainEnumAndAnyCause() {
        Assertions.assertThatThrownBy(() -> new InvalidTransitionChainException(null, new NullPointerException())).isInstanceOf(NullPointerException.class);
    }
}
