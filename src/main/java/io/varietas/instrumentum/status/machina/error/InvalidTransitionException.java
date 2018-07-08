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
package io.varietas.instrumentum.status.machina.error;

import java.util.Objects;
import lombok.NonNull;

/**
 * <h2>InvalidTransitionException</h2>
 * <p>
 * Signals the triggering of a FSMs by an invalid transition. The reasons can be e.g. not present transition or mismatching start state.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/8/2017
 */
public class InvalidTransitionException extends RuntimeException {

    private final Enum transition;

    public InvalidTransitionException(@NonNull final Enum transition, @NonNull final String message) {
        super(message);
        this.transition = transition;
    }

    public InvalidTransitionException(@NonNull final Enum transition, @NonNull final String message, final Throwable cause) {
        super(message, cause);
        this.transition = transition;
    }

    @Override
    public String getLocalizedMessage() {
        StringBuilder builder = new StringBuilder("Transition '")
                .append(transition.name())
                .append("' isn't possible: ")
                .append(this.getMessage())
                .append('.');
        if (Objects.nonNull(super.getCause())) {
            builder
                    .append(' ')
                    .append(super.getCause().getClass().getSimpleName())
                    .append(": ")
                    .append(super.getCause().getLocalizedMessage())
                    .append('.');
        }

        return builder.toString();
    }
}
