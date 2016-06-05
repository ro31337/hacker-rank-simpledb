package org.simpledb;

/**
 * Numequalto command
 * Returns the number of variables that are currently set to `value`.
 * If no variables equal that value, returns 0.
 */
public class NumEqualToCommand implements Command {

    private String[] args;
    public NumEqualToCommand(String[] args) {
        this.args = args;
    }

    @Override
    public Response execute(TransactionLayer database) {
        if(args.length != 2)
            return new WrongArgsResponse(database);
        Integer value = database.numEqualTo(args[1]);
        if(value == null)
            value = 0;
        return new TextResponse(value.toString(), database);
    }
}
