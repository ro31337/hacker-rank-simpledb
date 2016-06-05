package org.simpledb;

/**
 * Wrong number of arguments command response
 */
public class WrongArgsResponse extends BaseResponse implements Response {
    public WrongArgsResponse(TransactionLayer database) {
        super(database);
    }

    @Override
    public String toString() {
        return "Wrong number of arguments";
    }
}
