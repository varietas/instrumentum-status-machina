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
package io.varietas.instrumentum.status.machina.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h2>TransitionListener</h2>
 * <p>
 * Transition listeners allow the execution of methods before and/or after a transition. Attention: There is no interface available. The methods have to be written as shown below:
 * <pre>
 * <code>
 *
 * public void before(TransitionTypes transition, Model target){
 *     // Do something.
 * }
 *
 * public void after(TransitionTypes transition, Model target){
 *     // Do something.
 * }
 * </code>
 * </pre>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 10/27/2017
 */
@Documented
@Repeatable(TransitionListeners.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TransitionListener {

    Class<?> value();
}
