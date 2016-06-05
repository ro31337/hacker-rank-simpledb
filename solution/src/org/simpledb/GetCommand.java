package org.simpledb;

/**
 * Get command
 */
public class GetCommand implements Command {

    private String[] args;
    public GetCommand(String[] args) {
        this.args = args;
    }

    @Override
    public Response execute(TransactionLayer database) {
        if(args.length != 2)
            return new WrongArgsResponse(database);
        String value = database.get(args[1]);
        if(value == null) value = "NULL";
        return new TextResponse(value, database);
    }
}
