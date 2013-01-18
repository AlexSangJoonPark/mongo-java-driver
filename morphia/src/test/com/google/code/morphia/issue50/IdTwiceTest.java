/*
 * Copyright (c) 2008 - 2012 10gen, Inc. <http://10gen.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.code.morphia.issue50;

import com.google.code.morphia.TestBase;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.mapping.validation.ConstraintViolationException;
import com.google.code.morphia.testutil.TestEntity;
import org.junit.Test;

import static org.junit.Assert.fail;

public class IdTwiceTest extends TestBase {

    @Test
    public final void testRedundantId() {
        try {
            morphia.map(A.class);
            fail();
        } catch (ConstraintViolationException expected) {
            // fine
        }
    }

    static class A extends TestEntity {
        private static final long serialVersionUID = 1L;
        @Id
        private String extraId;
        @Id
        private String broken;
    }

}
