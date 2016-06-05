package org.simpledb;

/**
 * Default transaction layer. Contains full implementation of:
 * begin, commit, rollback
 * Also works as a proxy with underlying database and keeps local diff.
 */
public class DefaultTransactionLayer extends BaseDatabase implements TransactionLayer {

    private TransactionLayer database;

    public DefaultTransactionLayer(TransactionLayer database) {
        super();
        this.database = database;
    }

    @Override
    public TransactionLayer begin() {
        return new DefaultTransactionLayer(this);
    }

    @Override
    public TransactionLayer commit() {
        StorageChanges changes = new StorageChanges(kv, stats);
        database.apply(changes);
        return database.commit();
    }

    @Override
    public TransactionLayer rollback() {
        return database;
    }

    @Override
    public Boolean isComplete() {
        return true;
    }

    @Override
    public String get(String name) {
        if(kv.containsKey(name))
            return kv.get(name);
        else
            return database.get(name); // get from underlying database
    }

    @Override
    public Integer numEqualTo(String value) {
        String key = value;
        if(stats.containsKey(key))
            return stats.get(key);
        else
            return database.numEqualTo(key); // get from underlying storage
    }

    @Override
    void kvRemove(String key) {
        kv.put(key, null);
    }

    @Override
    void statsRemove(String key) {
        stats.put(key, null);
    }

    @Override
    void statsRecordHook(String k) {
        // ensure the stats record is loaded from underlying database
        if(!stats.containsKey(k)) {
            Integer value = database.numEqualTo(k);
            stats.put(k, value);
        }
    }
}
