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

import org.junit.jupiter.api.Test;

/**
 *
 * @author Michael Rhöse
 */
public class InvalidTransitionListenerExceptionTest {
    
    public InvalidTransitionListenerExceptionTest() {
    }

    /**
     * Test of getLocalizedMessage method, of class InvalidTransitionListenerException.
     */
    @Test
    public void testGetLocalizedMessage() {
        System.out.println("getLocalizedMessage");
        InvalidTransitionListenerException instance = null;
        String expResult = "";
        String result = instance.getLocalizedMessage();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
