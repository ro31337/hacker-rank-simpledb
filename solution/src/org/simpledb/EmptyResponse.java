package org.simpledb;

/**
 * Empty command response
 */
public class EmptyResponse extends BaseResponse implements Response {
    public EmptyResponse(TransactionLayer database) {
        super(database);
    }

    @Override
    public String toString() {
        return "";
    }
}
