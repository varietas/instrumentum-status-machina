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
package io.varietas.instrumentum.status.machina.listeners;

import io.varietas.instrumentum.status.machina.models.ExampleChain;
import io.varietas.instrumentum.status.machina.models.TestEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>SimpleChainBeforeListener</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 10/27/2017
 */
@Slf4j
public class SimpleChainBeforeListener {

    public void before(final ExampleChain chain, final TestEntity target) {
        target.setValue(target.getValue() + 101);
    }
}
