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
 * <h2>io.varietas.common.fsm</h2>
 *
 * This finite state machine implementation based on the <a href="https://www.innoq.com/de/blog/statemachine/">post</a> of Florian Miess for a the simplest as possible state machine in Java. The fsm
 * is used to manage the life cycle of plugins in varietas.io. There are different specialised implementations which supports the event bus and allows the execution of transition chains.
 *
 * @author Michael Rhöse
 * @version 1.0.0, 10/7/2017
 */
package io.varietas.instrumentum.status.machina;
