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
package io.varietas.instrumentum.status.machina.error;

import java.util.Objects;

/**
 * <h2>IllegalContainerException</h2>
 * <p>
 * The exception signals the use of an object with an unexpected type.
 *
 * @author Michael Rhöse
 * @version 1.0.1.0, 08/08/2019
 */
public class UnexpectedArgumentException extends RuntimeException {

    private Class<?> type;
    private String message;

    public UnexpectedArgumentException(final Object object) {
        this.type = object.getClass();
    }

    public UnexpectedArgumentException(final Object object, final String message) {
        this.message = message;
        this.type = object.getClass();
    }

    public UnexpectedArgumentException(final Object object, final String message, final Throwable cause) {
        super(cause);
        this.message = message;
        this.type = object.getClass();
    }

    @Override
    public String getLocalizedMessage() {

        final StringBuilder builder = new StringBuilder("Object of type '")
                .append(this.type.getCanonicalName())
                .append("' isn't allowed");

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
