package org.simpledb;

/**
 * Base command response
 */
public abstract class BaseResponse implements Response {

    private TransactionLayer database;

    public BaseResponse(TransactionLayer database) {
        this.database = database;
    }

    @Override
    public TransactionLayer database() {
        return database;
    }
}
