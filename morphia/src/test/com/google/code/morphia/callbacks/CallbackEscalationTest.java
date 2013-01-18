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

/**
 *
 */
package com.google.code.morphia.callbacks;

import com.google.code.morphia.TestBase;
import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.PostLoad;
import com.google.code.morphia.annotations.PostPersist;
import com.google.code.morphia.annotations.PreLoad;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.PreSave;
import com.google.code.morphia.annotations.Transient;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class CallbackEscalationTest extends TestBase {
    @Entity
    static class A extends Callbacks {
        @Id
        private ObjectId id;

        @Embedded
        private B b;

        @Embedded
        private final List<B> bs = new LinkedList<B>();
    }

    @Embedded
    static class B extends Callbacks {
        // minor issue: i realized, that if B does not bring anything to map,
        // morphia behaves significantly different, is this wanted ?
        // see TestEmptyEntityMapping
        private final String someProperty = "someThing";
    }

    static class Callbacks {
        @Transient
        boolean prePersist, postPersist, preLoad, postLoad, preSave;

        @PrePersist
        void prePersist() {
            this.prePersist = true;
        }

        @PostPersist
        void postPersist() {
            this.postPersist = true;
        }

        @PreLoad
        void preLoad() {
            this.preLoad = true;
        }

        @PostLoad
        void postLoad() {
            this.postLoad = true;
        }

        @PreSave
        void preSave() {
            this.preSave = true;
        }
    }

    @Test
    public void testPrePersistEscalation() throws Exception {
        final A a = new A();
        a.b = new B();
        a.bs.add(new B());

        Assert.assertFalse(a.prePersist);
        Assert.assertFalse(a.b.prePersist);
        Assert.assertFalse(a.bs.get(0).prePersist);

        this.ds.save(a);

        Assert.assertTrue(a.prePersist);
        Assert.assertTrue(a.b.prePersist);
        Assert.assertTrue(a.bs.get(0).prePersist);
    }

    @Test
    public void testPostPersistEscalation() throws Exception {
        final A a = new A();
        a.b = new B();
        a.bs.add(new B());

        Assert.assertFalse(a.postPersist);
        Assert.assertFalse(a.b.postPersist);
        Assert.assertFalse(a.bs.get(0).postPersist);

        this.ds.save(a);

        Assert.assertTrue(a.preSave);
        Assert.assertTrue(a.postPersist);
        Assert.assertTrue(a.b.preSave);
        Assert.assertTrue(a.b.postPersist); //PostPersist in not only called on entities
        Assert.assertTrue(a.bs.get(0).preSave);
        Assert.assertTrue(a.bs.get(0).postPersist); //PostPersist is not only called on entities
    }

    @Test
    public void testPreLoadEscalation() throws Exception {
        A a = new A();
        a.b = new B();
        a.bs.add(new B());

        Assert.assertFalse(a.preLoad);
        Assert.assertFalse(a.b.preLoad);
        Assert.assertFalse(a.bs.get(0).preLoad);

        this.ds.save(a);

        Assert.assertFalse(a.preLoad);
        Assert.assertFalse(a.b.preLoad);
        Assert.assertFalse(a.bs.get(0).preLoad);

        a = this.ds.find(A.class, "_id", a.id).get();

        Assert.assertTrue(a.preLoad);
        Assert.assertTrue(a.b.preLoad);
        Assert.assertTrue(a.bs.get(0).preLoad);

    }

    @Test
    public void testPostLoadEscalation() throws Exception {
        A a = new A();
        a.b = new B();
        a.bs.add(new B());

        Assert.assertFalse(a.postLoad);
        Assert.assertFalse(a.b.postLoad);
        Assert.assertFalse(a.bs.get(0).postLoad);

        this.ds.save(a);

        Assert.assertFalse(a.preLoad);
        Assert.assertFalse(a.b.preLoad);
        Assert.assertFalse(a.bs.get(0).preLoad);

        a = this.ds.find(A.class, "_id", a.id).get();

        Assert.assertTrue(a.postLoad);
        Assert.assertTrue(a.b.postLoad);
        Assert.assertTrue(a.bs.get(0).postLoad);

    }
}
