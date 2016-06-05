package org.simpledb;

import java.util.HashMap;

/**
 * Base methods for database operations
 */
abstract class BaseDatabase implements Database {

    // POSSIBLE AREA OF IMPROVEMENT:
    // We can implement our own HashMap with with md5 keys instead of String keys.
    // This approach will save memory for `kv` and `stats` when keys are lengthy (more than 32 bytes).

    HashMap<String, String> kv;
    HashMap<String, Integer> stats;

    BaseDatabase() {
        this.kv = new HashMap<String, String>();
        this.stats = new HashMap<String, Integer>();
    }

    @Override
    public void set(String name, String value) {
        unset(name);
        kv.put(name, value);
        increaseStatsRecord(value);
    }

    @Override
    public void unset(String name) {
        String value = get(name);
        decreaseStatsRecord(value);
        kvRemove(name);
    }

    @Override
    public void apply(StorageChanges changes) {
        // apply kv changes
        changes.kv().forEach((k, v) -> {
            if(v == null)
                kv.remove(k);
            else
                kv.put(k, v);
        });

        // apply stats changes
        changes.stats().forEach((k, v) -> {
            if(v == null)
                stats.remove(k);
            else
                stats.put(k, v);
        });
    }

    /**
     * Increase `value` count for `numequalto` command.
     */
    private void increaseStatsRecord(String key) {
        if(key == null || key.isEmpty()) return;
        statsRecordHook(key);
        Integer record = stats.get(key);
        if(record == null)
            record = 0;
        stats.put(key, record + 1);
    }

    /**
     * Decrease `value` count for `numequalto` command.
     */
    private void decreaseStatsRecord(String key) {
        if(key == null || key.isEmpty()) return;
        statsRecordHook(key);
        Integer record = stats.get(key);
        if(record != null) {
            if(record <= 1)
                statsRemove(key);
            else
                stats.put(key, record - 1);
        }
    }

    /**
     * Removes the value from kv storage.
     * Can be overridden to replace removal with setting to null.
     */
    void kvRemove(String key) {
        kv.remove(key);
    }

    /**
     * Removes the value from stats storage.
     * Can be overridden to replace removal with setting to null.
     */
    void statsRemove(String key) {
        stats.remove(key);
    }

    /**
     * Stats record access hook.
     * Can be overridden in transaction to load local copy of stats record.
     */
    void statsRecordHook(String key) {
    }
}
