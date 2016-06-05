package org.simpledb;

/**
 * Unset command
 */
public class UnsetCommand implements Command {

    private String[] args;
    public UnsetCommand(String[] args) {
        this.args = args;
    }

    @Override
    public Response execute(TransactionLayer database) {
        if(args.length != 2)
            return new WrongArgsResponse(database);
        database.unset(args[1]);
        return new EmptyResponse(database);
    }
}
