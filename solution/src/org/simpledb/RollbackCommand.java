package org.simpledb;

/**
 * Rollback command
 */
public class RollbackCommand implements Command {

    private String[] args;
    public RollbackCommand(String[] args) {
        this.args = args;
    }

    @Override
    public Response execute(TransactionLayer database) {
        if(args.length != 1)
            return new WrongArgsResponse(database);
        if(database.isComplete())
            return new EmptyResponse(database.rollback());
        else
            return new TextResponse("NO TRANSACTION", database);
    }
}
