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
package io.varietas.instrumentum.status.machina.errors;

import io.varietas.instrumentum.status.machina.errors.UnexpectedArgumentException;
import io.varietas.instrumentum.status.machina.models.ExampleChain;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Michael Rhöse
 */
public class UnexpectedArgumentExceptionTest {

    @Test
    public void testGetLocalizedMessage() {
        UnexpectedArgumentException instance = new UnexpectedArgumentException(ExampleChain.INSTALLING);
        String expResult = "Object of type 'io.varietas.instrumentum.status.machina.models.ExampleChain' isn't allowed'.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetLocalizedMessageWithAdditionalMessage() {
        UnexpectedArgumentException instance = new UnexpectedArgumentException(ExampleChain.INSTALLING, "Any message");
        String expResult = "Object of type 'io.varietas.instrumentum.status.machina.models.ExampleChain' isn't allowed': Any message.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetLocalizedMessageWithAdditionalMessageAndCause() {
        UnexpectedArgumentException instance = new UnexpectedArgumentException(ExampleChain.INSTALLING, "Any message", new NullPointerException("Any null pointer"));
        String expResult = "Object of type 'io.varietas.instrumentum.status.machina.models.ExampleChain' isn't allowed': Any message. NullPointerException: Any null pointer.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetLocalizedMessageWithAdditionalCause() {
        UnexpectedArgumentException instance = new UnexpectedArgumentException(ExampleChain.INSTALLING, null);
        String expResult = "Object of type 'io.varietas.instrumentum.status.machina.models.ExampleChain' isn't allowed'.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testNUllObject() {
        Assertions.assertThatThrownBy(() -> new UnexpectedArgumentException(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNUllObjectAndAnyMessage() {
        Assertions.assertThatThrownBy(() -> new UnexpectedArgumentException(null, "")).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNUllObjectAndAnyMessageAndAnyCause() {
        Assertions.assertThatThrownBy(() -> new UnexpectedArgumentException(null, "", new NullPointerException())).isInstanceOf(NullPointerException.class);
    }
}
