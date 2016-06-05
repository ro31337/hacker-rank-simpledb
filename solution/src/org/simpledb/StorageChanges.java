package org.simpledb;

import java.util.HashMap;

/**
 * Transaction storage changes (diff)
 */
public final class StorageChanges {
    private HashMap<String, String> kv;
    private HashMap<String, Integer> stats;

    public StorageChanges(HashMap<String, String> kv, HashMap<String, Integer> stats) {
        this.kv = kv;
        this.stats = stats;
    }

    public HashMap<String, String> kv() {
        return kv;
    }

    public HashMap<String, Integer> stats() {
        return stats;
    }
}
