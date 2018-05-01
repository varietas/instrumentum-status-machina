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

    public TransitionChainCreationException(boolean direction, String from, String to, String chain) {
        this.direction = direction;
        this.from = from;
        this.to = to;
        this.chain = chain;
    }

    public TransitionChainCreationException(boolean direction, String from, String to, String chain, String message) {
        super(message);
        this.direction = direction;
        this.from = from;
        this.to = to;
        this.chain = chain;
    }

    public TransitionChainCreationException(boolean direction, String from, String to, String chain, String message, Throwable cause) {
        super(message, cause);
        this.direction = direction;
        this.from = from;
        this.to = to;
        this.chain = chain;
    }

    public TransitionChainCreationException(boolean direction, String from, String to, String chain, Throwable cause) {
        super(cause);
        this.direction = direction;
        this.from = from;
        this.to = to;
        this.chain = chain;
    }

    public TransitionChainCreationException(boolean direction, String from, String to, String chain, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.direction = direction;
        this.from = from;
        this.to = to;
        this.chain = chain;
    }

    @Override
    public String getLocalizedMessage() {
        return "Couldn't create chain '" + this.chain + "'. "
            + "There is now transition available from " + ((this.direction) ? this.from + " to " + this.to : this.to + " to " + this.from) + ". "
            + ((Objects.isNull(this.getMessage())) ? "" : this.getMessage());
    }
}
