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
 * <h2>InvalidTransitionError</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 10/8/2017
 */
public class InvalidTransitionChainException extends Exception {

    private final Enum chain;
    private String message;

    public InvalidTransitionChainException(@NonNull final Enum chain) {
        this.chain = chain;
    }

    public InvalidTransitionChainException(@NonNull final Enum chain, final String message) {
        this.chain = chain;
        this.message = message;
    }

    public InvalidTransitionChainException(@NonNull final Enum chain, final String message, final Throwable cause) {
        super(cause);
        this.chain = chain;
        this.message = message;
    }

    public InvalidTransitionChainException(@NonNull final Enum chain, final Throwable cause) {
        super(cause);
        this.chain = chain;
    }

    @Override
    public String getLocalizedMessage() {

        final StringBuilder builder = new StringBuilder("Chain '")
                .append(this.chain.name())
                .append("' isn't possible");

        if (Objects.nonNull(this.message)) {
            builder.append("': ").append(this.message).append('.');
        } else {
            builder.append("'.");
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
