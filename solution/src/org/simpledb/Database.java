package org.simpledb;

/**
 * Database interface
 */
public interface Database {
    void set(String name, String value);
    String get(String name);
    void unset(String name);
    Integer numEqualTo(String value);
    void apply(StorageChanges changes);
}
