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
package io.varietas.instrumentum.status.machina.models;

import io.varietas.instrumentum.status.machina.StatedObject;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <h2>TestEntity</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/31/2017
 */
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class TestEntity implements StatedObject<ExampleState> {

    private ExampleState state;
    @Getter
    @Setter
    private int value;

    @Override
    public ExampleState state() {
        return this.state;
    }

    @Override
    public void state(ExampleState state) {
        this.state = state;
    }
}
