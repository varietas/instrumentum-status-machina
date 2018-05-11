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

/**
 * <h2>InvalidTransitionException</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 10/8/2017
 */
public class InvalidTransitionException extends RuntimeException {

    private final Enum transition;

    public InvalidTransitionException(Enum transition) {
        this.transition = transition;
    }

    public InvalidTransitionException(Enum transition, String message) {
        super(message);
        this.transition = transition;
    }

    public InvalidTransitionException(Enum transition, String message, Throwable cause) {
        super(message, cause);
        this.transition = transition;
    }

    public InvalidTransitionException(Enum transition, Throwable cause) {
        super(cause);
        this.transition = transition;
    }

    public InvalidTransitionException(Enum transition, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.transition = transition;
    }

    @Override
    public String getLocalizedMessage() {
        return "Transition '" + transition.name() + "' isn't possible. "
                + ((Objects.isNull(this.getMessage())) ? "" : this.getMessage());
    }
}