/*
 * Copyright (c) 2008-2014 MongoDB, Inc.
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


import org.mongodb.CommandResult;
import org.mongodb.MongoCursor;
import org.mongodb.ServerCursor;
import org.mongodb.connection.ServerAddress;

import java.util.Iterator;
import java.util.List;


public class InlineMongoCursor<T> implements MongoCursor<T> {
    private final CommandResult commandResult;
    private final List<T> results;
    private final Iterator<T> iterator;

    public InlineMongoCursor(final CommandResult result, final List<T> results) {
        commandResult = result;
        this.results = results;
        iterator = this.results.iterator();
    }

    public CommandResult getCommandResult() {
        return commandResult;
    }

    @Override
    public void close() {
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    @Override
    public ServerCursor getServerCursor() {
        return null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Inline aggregations don't support remove operations.");
    }

    @Override
    public ServerAddress getServerAddress() {
        return commandResult.getAddress();
    }
}