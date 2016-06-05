package org.simpledb;

/**
 * Set command
 */
public class SetCommand implements Command {

    private String[] args;
    public SetCommand(String[] args) {
        this.args = args;
    }

    @Override
    public Response execute(TransactionLayer database) {
        if(args.length != 3)
            return new WrongArgsResponse(database);
        database.set(args[1], args[2]);
        return new EmptyResponse(database);
    }
}
