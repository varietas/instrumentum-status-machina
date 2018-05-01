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

import io.varietas.instrumentum.status.machina.StateMachine;
import java.util.Objects;

/**
 * <h2>MachineCreationException</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 10/9/2017
 */
public class MachineCreationException extends Exception {

    private Class<? extends StateMachine> machineType;

    public MachineCreationException(Class<? extends StateMachine> machineType) {
        this.machineType = machineType;
    }

    public MachineCreationException(Class<? extends StateMachine> machineType, String message) {
        super(message);
        this.machineType = machineType;
    }

    public MachineCreationException(Class<? extends StateMachine> machineType, String message, Throwable cause) {
        super(message, cause);
        this.machineType = machineType;
    }

    @Override
    public String getLocalizedMessage() {
        return "Couldn't create state machine '" + this.machineType.getName() + "'. "
            + ((Objects.isNull(this.getMessage())) ? "" : this.getMessage());
    }
}
