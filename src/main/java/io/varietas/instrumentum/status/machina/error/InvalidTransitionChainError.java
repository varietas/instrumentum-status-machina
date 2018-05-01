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
 * <h2>InvalidTransitionError</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 10/8/2017
 */
public class InvalidTransitionChainError extends Error {

    private final Enum chain;

    public InvalidTransitionChainError(Enum chain) {
        this.chain = chain;
    }

    public InvalidTransitionChainError(Enum chain, String message) {
        super(message);
        this.chain = chain;
    }

    public InvalidTransitionChainError(Enum chain, String message, Throwable cause) {
        super(message, cause);
        this.chain = chain;
    }

    public InvalidTransitionChainError(Enum chain, Throwable cause) {
        super(cause);
        this.chain = chain;
    }

    public InvalidTransitionChainError(Enum chain, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.chain = chain;
    }

    @Override
    public String getLocalizedMessage() {
        return "Chain '" + chain.name() + "' isn't possible. "
            + ((Objects.isNull(this.getMessage())) ? "" : this.getMessage());
    }
}
