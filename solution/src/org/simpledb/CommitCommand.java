package org.simpledb;

/**
 * Commit command
 */
public class CommitCommand implements Command {

    private String[] args;
    public CommitCommand(String[] args) {
        this.args = args;
    }

    @Override
    public Response execute(TransactionLayer database) {
        if(args.length != 1)
            return new WrongArgsResponse(database);
        if(database.isComplete())
            return new EmptyResponse(database.commit());
        else
            return new TextResponse("NO TRANSACTION", database);
    }
}
