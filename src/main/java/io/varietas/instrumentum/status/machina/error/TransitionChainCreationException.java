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
 * <h2>TransitionChainCreationException</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 10/10/2017
 */
public class TransitionChainCreationException extends RuntimeException {

    private final boolean direction;
    private final String from;
    private final String to;
    private final String chain;
    private String message;

    public TransitionChainCreationException(final boolean direction, @NonNull final String from, @NonNull final String to, @NonNull final String chain) {
        this.direction = direction;
        this.from = from;
        this.to = to;
        this.chain = chain;
    }

    public TransitionChainCreationException(final boolean direction, @NonNull final String from, @NonNull final String to, @NonNull final String chain, final String message) {
        this.direction = direction;
        this.from = from;
        this.to = to;
        this.chain = chain;
        this.message = message;
    }

    public TransitionChainCreationException(final boolean direction, @NonNull final String from, @NonNull final String to, @NonNull final String chain, final String message, final Throwable cause) {
        super(cause);
        this.direction = direction;
        this.from = from;
        this.to = to;
        this.chain = chain;
        this.message = message;
    }

    public TransitionChainCreationException(final boolean direction, @NonNull final String from, @NonNull final String to, @NonNull final String chain, final Throwable cause) {
        super(cause);
        this.direction = direction;
        this.from = from;
        this.to = to;
        this.chain = chain;
    }

    @Override
    public String getLocalizedMessage() {

        final StringBuilder builder = new StringBuilder("Couldn't create chain '")
                .append(this.chain)
                .append("'. There is now transition available from '");

        if (this.direction) {
            builder.append(this.from).append("' to '").append(this.to);
        } else {
            builder.append(this.to).append("' to '").append(this.from);
        }

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
