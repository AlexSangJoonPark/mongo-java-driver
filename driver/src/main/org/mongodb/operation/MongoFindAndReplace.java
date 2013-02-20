/*
 * Copyright (c) 2008 - 2013 10gen, Inc. <http://10gen.com>
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

package org.mongodb.operation;

import org.bson.types.Document;

public class MongoFindAndReplace<T> extends MongoFindAndModify {
    private final T replacement;

    public MongoFindAndReplace(final T replacement) {
        this.replacement = replacement;
    }

    public T getReplacement() {
        return replacement;
    }

    @Override
    public MongoFindAndReplace<T> sortBy(final MongoSortCriteria sortCriteria) {
        super.sortBy(sortCriteria);
        return this;
    }

    @Override
    public MongoFindAndReplace<T> returnNew(final boolean returnNew) {
        super.returnNew(returnNew);
        return this;
    }

    @Override
    public MongoFindAndReplace<T> upsert(final boolean upsert) {
        super.upsert(upsert);
        return this;
    }

    @Override
    public MongoFindAndReplace<T> where(final Document filter) {
        super.where(filter);
        return this;
    }

    @Override
    public MongoFindAndReplace<T> select(final MongoFieldSelector selector) {
        super.select(selector);
        return this;
    }

}