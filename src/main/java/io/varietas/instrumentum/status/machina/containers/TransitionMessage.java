/*
 * Copyright 2017 Michael Rhöse.
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

import io.varietas.instrumentum.status.machina.Statable;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * <h2>TransitionChainMessage</h2>
 * <p>
 * This class is a container to send an object (transition target) and its transition event over the event bus.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/13/2017
 */
@ToString
@EqualsAndHashCode
@Value(staticConstructor = "of")
public class TransitionMessage {

    @NonNull
    Enum<?> transition;

    @NonNull
    Statable<? extends Enum<?>> target;
}
