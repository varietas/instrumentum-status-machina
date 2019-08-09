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
 * The exception is thrown by the {@link io.varietas.instrumentum.status.machina.AbstractStateMachine} while executing a listener for a transition. The reasons are:
 * <ul>
 * <li>{@link NoSuchMethodException}</li>
 * <li>{@link SecurityException}</li>
 * <li>{@link IllegalAccessException}</li>
 * <li>{@link IllegalArgumentException}</li>
 * <li>{@link InvocationTargetException}</li>
 * <li>{@link InstantiationException}</li>
 * </ul>
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/8/2017
 */
public final class InvalidTransitionListenerException extends RuntimeException {

    private final Class<?> listenerType;

    public InvalidTransitionListenerException(@NonNull final Class<?> listenerType, @NonNull final String message) {
        super(message);
        this.listenerType = listenerType;
    }

    public InvalidTransitionListenerException(@NonNull final Class<?> listenerType, @NonNull final String message, final Throwable cause) {
        super(message, cause);
        this.listenerType = listenerType;
    }

    @Override
    public String getLocalizedMessage() {
        StringBuilder builder = new StringBuilder("Transition listener [")
                .append(this.listenerType.getName())
                .append("] not executable.");

        if (Objects.nonNull(super.getMessage())) {
            builder
                    .append(' ')
                    .append(this.getMessage());
        }

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
