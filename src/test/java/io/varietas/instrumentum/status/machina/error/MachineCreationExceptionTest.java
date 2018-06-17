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

import io.varietas.instrumentum.status.machina.machines.transition.FailingStateMachine;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 * @author Michael Rhöse
 */
@RunWith(JUnit4.class)
public class MachineCreationExceptionTest {

    @Test
    public void testGetLocalizedMessage() {
        MachineCreationException instance = new MachineCreationException(FailingStateMachine.class);
        String expResult = "Couldn't create state machine 'io.varietas.instrumentum.status.machina.machines.transition.FailingStateMachine'.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetLocalizedMessageWithAdditionalMessage() {
        MachineCreationException instance = new MachineCreationException(FailingStateMachine.class, "Any message");
        String expResult = "Couldn't create state machine 'io.varietas.instrumentum.status.machina.machines.transition.FailingStateMachine': Any message.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testGetLocalizedMessageWithAdditionalMessageAndCause() {
        MachineCreationException instance = new MachineCreationException(FailingStateMachine.class, "Any message", new NullPointerException("Nested message"));
        String expResult = "Couldn't create state machine 'io.varietas.instrumentum.status.machina.machines.transition.FailingStateMachine': Any message. NullPointerException: Nested message.";
        String result = instance.getLocalizedMessage();
        Assertions.assertThat(result).isEqualTo(expResult);
    }

    @Test
    public void testNullStateMachineType() {
        Assertions.assertThatThrownBy(() -> new MachineCreationException(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNullStateMachineTypeAndAnyMessage() {
        Assertions.assertThatThrownBy(() -> new MachineCreationException(null, "")).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testNullStateMachineTypeAndAnyMessageAndCause() {
        Assertions.assertThatThrownBy(() -> new MachineCreationException(null, "", new NullPointerException())).isInstanceOf(NullPointerException.class);
    }
}
