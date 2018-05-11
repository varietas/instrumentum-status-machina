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
/**
 * <h2>io.varietas.instrumentum.status.machina</h2>
 * <p>
 * This finite state machine implementation is based on the
 * <a href="https://www.innoq.com/de/blog/statemachine/">post</a> of Florian Miess for a the simplest as possible state machine in Java. The FSM stores the current state within the object. An
 * additional object storing state information isn't required. This implementation also provides transition chaining and CbA (configuration by annotation).</br>
 * Each transition logic is placed in a testable method and allows the testing while build time.
 *
 * @author Michael Rhöse
 * @version 1.0.0, 10/7/2017
 */
package io.varietas.instrumentum.status.machina;
