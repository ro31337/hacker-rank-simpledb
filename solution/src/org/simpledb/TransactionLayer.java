package org.simpledb;

/**
 * Interface to support database transactions
 */
public interface TransactionLayer extends Database {
    /**
     * Begins transaction and saves snapshot of existing database
     */
    TransactionLayer begin();

    /**
     * Commits changes to the database
     */
    TransactionLayer commit();

    /**
     * Rolls back database to previous state
     */
    TransactionLayer rollback();

    /**
     * Flag indicating if transaction layer is complete and fully implemented
     * (begin, commit, rollback)
     */
    Boolean isComplete();
}
