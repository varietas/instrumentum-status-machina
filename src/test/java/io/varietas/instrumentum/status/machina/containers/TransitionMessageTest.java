/*
 * Copyright 2021 Michael Rhöse.
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

import io.varietas.instrumentum.status.machina.models.ExampleState;
import io.varietas.instrumentum.status.machina.models.TestEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Michael Rhöse
 */
public class TransitionMessageTest {

    @Test
    public void of_createInstance_transitionNull_throwsException() {

        final Enum<?> transition = null;
        final TestEntity target = TestEntity.of(ExampleState.ACTIVATED, 0);

        final Throwable result = Assertions.catchThrowable(() -> TransitionMessage.of(transition, target));

        Assertions.assertThat(result).isInstanceOf(NullPointerException.class).hasMessage("transition is marked non-null but is null");
    }

    @Test
    public void of_createInstance_targetNull_throwsException() {

        final Enum<?> transition = ExampleState.ACTIVATED;
        final TestEntity target = null;

        final Throwable result = Assertions.catchThrowable(() -> TransitionMessage.of(transition, target));

        Assertions.assertThat(result).isInstanceOf(NullPointerException.class).hasMessage("target is marked non-null but is null");
    }

    @Test
    public void of_createInstance_transitionAndTargetAreValid_resultsInValidInstance() {

        final Enum<?> transition = ExampleState.ACTIVATED;
        final TestEntity target = TestEntity.of(ExampleState.ACTIVATED, 0);

        final TransitionMessage result = TransitionMessage.of(transition, target);

        Assertions.assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("target", target)
                .hasFieldOrPropertyWithValue("transition", transition);
    }

    @Test
    public void of_toString_transitionAndTargetAreValid_resultsInReadableString() {

        final Enum<?> transition = ExampleState.ACTIVATED;
        final TestEntity target = TestEntity.of(ExampleState.ACTIVATED, 0);

        final TransitionMessage result = TransitionMessage.of(transition, target);

        Assertions.assertThat(result).isNotNull()
                .hasToString("TransitionMessage(transition=ACTIVATED, target=TestEntity(state=ACTIVATED, value=0))");
    }

    @Test
    public void of_hashCode_givenAndExpectedHaveSameValues_resultsInEqualHash() {

        final Enum<?> transition = ExampleState.ACTIVATED;
        final TestEntity target = TestEntity.of(ExampleState.ACTIVATED, 0);

        final TransitionMessage result = TransitionMessage.of(transition, target);
        final TransitionMessage expected = TransitionMessage.of(transition, target);

        Assertions.assertThat(result).isNotNull()
                .hasSameHashCodeAs(expected);
    }

    @Test
    public void of_hashCode_givenAndExpectedHaveDifferentValues_resultsInDifferentHash() {

        final Enum<?> transition1 = ExampleState.ACTIVATED;
        final Enum<?> transition2 = ExampleState.DEACTIVATED;
        final TestEntity target = TestEntity.of(ExampleState.ACTIVATED, 0);

        final TransitionMessage result = TransitionMessage.of(transition1, target);
        final TransitionMessage expected = TransitionMessage.of(transition2, target);

        Assertions.assertThat(result.hashCode()).isNotEqualTo(expected.hashCode());
    }

    @Test
    public void of_equals_givenAndExpectedHaveSameValues_resultsInEqualIsTrue() {

        final Enum<?> transition = ExampleState.ACTIVATED;
        final TestEntity target = TestEntity.of(ExampleState.ACTIVATED, 0);

        final TransitionMessage msg1 = TransitionMessage.of(transition, target);
        final TransitionMessage msg2 = TransitionMessage.of(transition, target);

        final boolean result = msg1.equals(msg2);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void of_equals_givenAndExpectedHaveDifferentValues_resultsInEqualIsFalse() {

        final Enum<?> transition1 = ExampleState.ACTIVATED;
        final Enum<?> transition2 = ExampleState.DEACTIVATED;
        final TestEntity target = TestEntity.of(ExampleState.ACTIVATED, 0);

        final TransitionMessage msg1 = TransitionMessage.of(transition1, target);
        final TransitionMessage msg2 = TransitionMessage.of(transition2, target);

        final boolean result = msg1.equals(msg2);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void of_equals_givenIsNonNullAndExpectedIsNull_resultsInEqualIsFalse() {

        final Enum<?> transition1 = ExampleState.ACTIVATED;
        final TestEntity target = TestEntity.of(ExampleState.ACTIVATED, 0);

        final TransitionMessage msg1 = TransitionMessage.of(transition1, target);
        final TransitionMessage msg2 = null;

        final boolean result = msg1.equals(msg2);

        Assertions.assertThat(result).isFalse();
    }
}
