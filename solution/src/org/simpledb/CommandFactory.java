package org.simpledb;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Command factory method
 */
public class CommandFactory {
    /**
     * Hash map of method -> builder function.
     * It gives slightly better performance O(1) than standard approach with abstract switch/case method O(n).
     */
    private static final HashMap<String, Function<String[], Command>> map;
    static {
        map = new HashMap<>();
        map.put("end", EndCommand::new);
        map.put("set", SetCommand::new);
        map.put("get", GetCommand::new);
        map.put("unset", UnsetCommand::new);
        map.put("begin", BeginCommand::new);
        map.put("commit", CommitCommand::new);
        map.put("rollback", RollbackCommand::new);
        map.put("numequalto", NumEqualToCommand::new);
    }

    /**
     * Factory method to parse and return command object.
     * @return Command to execute
     */
    public static Command Parse(String line) {
        String[] ss = line.split("(\\s+)"); // ...and remove empty entries
        if(ss.length == 0) return new NotFoundCommand();

        String method = ss[0].toLowerCase(); // getting method: get, put, etc.
        if(!map.containsKey(method)) return new NotFoundCommand();

        return map.get(method).apply(ss);
    }
}
