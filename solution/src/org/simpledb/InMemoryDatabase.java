package org.simpledb;

/**
 * In-memory database
 */

public class InMemoryDatabase extends BaseDatabase implements Database {

    public InMemoryDatabase() {
        super();
    }

    @Override
    public String get(String name) {
        return kv.get(name);
    }

    @Override
    public Integer numEqualTo(String value) {
        String key = value;
        return stats.get(key);
    }
}
