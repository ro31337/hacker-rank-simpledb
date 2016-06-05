package org.simpledb;

/**
 * End command
 */
public class EndCommand implements Command {

    private String[] args;
    public EndCommand(String[] args) {
        this.args = args;
    }

    @Override
    public Response execute(TransactionLayer database) {
        if(args.length != 1)
            return new WrongArgsResponse(database);
        System.exit(0);
        return null;
    }
}
