package org.simpledb;

/**
 * Command not found response
 */
public class NotFoundCommand implements Command {

    @Override
    public Response execute(TransactionLayer database) {
        return new TextResponse("Command not found", database);
    }
}
