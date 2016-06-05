package org.simpledb;

/**
 * Incomplete transaction layer. Contains implementation of `begin` only.
 * `commit` and `rollback` returns current instance.
 * Works as a proxy with underlying database.
 */
public class IncompleteTransactionLayer implements TransactionLayer {

    private Database database;
    public IncompleteTransactionLayer(Database database) {
        this.database = database;
    }

    @Override
    public TransactionLayer begin() {
        return new DefaultTransactionLayer(this);
    }

    @Override
    public TransactionLayer commit() {
        return this;
    }

    @Override
    public TransactionLayer rollback() {
        return this;
    }

    @Override
    public Boolean isComplete() {
        return false;
    }

    @Override
    public void set(String name, String value) {
        database.set(name, value);
    }

    @Override
    public String get(String name) {
        return database.get(name);
    }

    @Override
    public void unset(String name) {
        database.unset(name);
    }

    @Override
    public Integer numEqualTo(String value) {
        return database.numEqualTo(value);
    }

    @Override
    public void apply(StorageChanges changes) {
        database.apply(changes);
    }
}
