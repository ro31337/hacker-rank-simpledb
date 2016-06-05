package org.simpledb;

/**
 * Begin transaction command
 */
public class BeginCommand implements Command {

    private String[] args;
    public BeginCommand(String[] args) {
        this.args = args;
    }

    @Override
    public Response execute(TransactionLayer database) {
        if(args.length != 1)
            return new WrongArgsResponse(database);
        else
            return new EmptyResponse(database.begin());
    }
}
