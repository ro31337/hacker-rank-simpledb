import java.io.*;
import java.util.HashMap;
import java.util.HashMap;
import java.util.function.Function;

public class Solution {
    public static void main(String args[] ) throws Exception {
      String line;
      TransactionLayer database = new IncompleteTransactionLayer(new InMemoryDatabase());
      BufferedReader stdin =  new BufferedReader(new InputStreamReader(System.in));

      while((line = stdin.readLine()) != null) {
          Command command = CommandFactory.Parse(line);
          Response response = command.execute(database);
          database = response.database();
          String result = response.toString();
          if(!result.isEmpty()) System.out.println(result);
      }
    }
}

/**
 * Command response
 */
interface Response {
    /**
     * String representation of response
     */
    String toString();

    /**
     * Database with the new state
     */
    TransactionLayer database();
}

/**
 * Empty command response
 */
class EmptyResponse extends BaseResponse implements Response {
    public EmptyResponse(TransactionLayer database) {
        super(database);
    }

    @Override
    public String toString() {
        return "";
    }
}

/**
 * Text response
 */
class TextResponse extends BaseResponse implements Response {
    private final String text;

    public TextResponse(final String text, TransactionLayer database) {
        super(database);
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}

/**
 * Base command response
 */
abstract class BaseResponse implements Response {

    private TransactionLayer database;

    public BaseResponse(TransactionLayer database) {
        this.database = database;
    }

    @Override
    public TransactionLayer database() {
        return database;
    }
}

/**
 * Wrong number of arguments command response
 */
class WrongArgsResponse extends BaseResponse implements Response {
    public WrongArgsResponse(TransactionLayer database) {
        super(database);
    }

    @Override
    public String toString() {
        return "Wrong number of arguments";
    }
}

/**
 * Base interface for commands
 */
interface Command {
    Response execute(TransactionLayer database);
}

/**
 * Begin transaction command
 */
class BeginCommand implements Command {

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

/**
 * Commit command
 */
class CommitCommand implements Command {

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

/**
 * Rollback command
 */
class RollbackCommand implements Command {

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

/**
 * Set command
 */
class SetCommand implements Command {

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

/**
 * Get command
 */
class GetCommand implements Command {

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

/**
 * Numequalto command
 * Returns the number of variables that are currently set to `value`.
 * If no variables equal that value, returns 0.
 */
class NumEqualToCommand implements Command {

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

/**
 * Unset command
 */
class UnsetCommand implements Command {

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

/**
 * End command
 */
class EndCommand implements Command {

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

/**
 * "Not found command" null object pattern
 */
class NotFoundCommand implements Command {

    @Override
    public Response execute(TransactionLayer database) {
        return new TextResponse("Command not found", database);
    }
}

/**
 * Command factory method
 */
class CommandFactory {
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

/**
 * Database interface
 */
interface Database {
    void set(String name, String value);
    String get(String name);
    void unset(String name);
    Integer numEqualTo(String value);
    void apply(StorageChanges changes);
}


/**
 * In-memory database
 */

class InMemoryDatabase extends BaseDatabase implements Database {

    public InMemoryDatabase() {
        super();
    }

    @Override
    public String get(String name) {
        return kv.get(name);
    }

    @Override
    public Integer numEqualTo(String value) {
        String key = value;
        return stats.get(key);
    }
}

/**
 * Base methods for database operations
 */
abstract class BaseDatabase implements Database {

    // POSSIBLE AREA OF IMPROVEMENT:
    // We can implement our own HashMap with with md5 keys instead of String keys.
    // This approach will save memory for `kv` and `stats` when keys are lengthy (more than 32 bytes).

    HashMap<String, String> kv;
    HashMap<String, Integer> stats;

    BaseDatabase() {
        this.kv = new HashMap<String, String>();
        this.stats = new HashMap<String, Integer>();
    }

    @Override
    public void set(String name, String value) {
        unset(name);
        kv.put(name, value);
        increaseStatsRecord(value);
    }

    @Override
    public void unset(String name) {
        String value = get(name);
        decreaseStatsRecord(value);
        kvRemove(name);
    }

    @Override
    public void apply(StorageChanges changes) {
        // apply kv changes
        changes.kv().forEach((k, v) -> {
            if(v == null)
                kv.remove(k);
            else
                kv.put(k, v);
        });

        // apply stats changes
        changes.stats().forEach((k, v) -> {
            if(v == null)
                stats.remove(k);
            else
                stats.put(k, v);
        });
    }

    /**
     * Increase `value` count for `numequalto` command.
     */
    private void increaseStatsRecord(String key) {
        if(key == null || key.isEmpty()) return;
        statsRecordHook(key);
        Integer record = stats.get(key);
        if(record == null)
            record = 0;
        stats.put(key, record + 1);
    }

    /**
     * Decrease `value` count for `numequalto` command.
     */
    private void decreaseStatsRecord(String key) {
        if(key == null || key.isEmpty()) return;
        statsRecordHook(key);
        Integer record = stats.get(key);
        if(record != null) {
            if(record <= 1)
                statsRemove(key);
            else
                stats.put(key, record - 1);
        }
    }

    /**
     * Removes the value from kv storage.
     * Can be overridden to replace removal with setting to null.
     */
    void kvRemove(String key) {
        kv.remove(key);
    }

    /**
     * Removes the value from stats storage.
     * Can be overridden to replace removal with setting to null.
     */
    void statsRemove(String key) {
        stats.remove(key);
    }

    /**
     * Stats record access hook.
     * Can be overridden in transaction to load local copy of stats record.
     */
    void statsRecordHook(String key) {
    }
}

/**
 * Interface to support database transactions
 */
interface TransactionLayer extends Database {
    /**
     * Begins transaction and saves snapshot of existing database
     */
    TransactionLayer begin();

    /**
     * Commits changes to the database
     */
    TransactionLayer commit();

    /**
     * Rolls back database to previous state
     */
    TransactionLayer rollback();

    /**
     * Flag indicating if transaction layer is complete and fully implemented
     * (begin, commit, rollback)
     */
    Boolean isComplete();
}

/**
 * Incomplete transaction layer. Contains implementation of `begin` only.
 * `commit` and `rollback` returns current instance.
 * Works as a proxy with underlying database.
 */
class IncompleteTransactionLayer implements TransactionLayer {

    private Database database;
    public IncompleteTransactionLayer(Database database) {
        this.database = database;
    }

    @Override
    public TransactionLayer begin() {
        return new DefaultTransactionLayer(this);
    }

    @Override
    public TransactionLayer commit() {
        return this;
    }

    @Override
    public TransactionLayer rollback() {
        return this;
    }

    @Override
    public Boolean isComplete() {
        return false;
    }

    @Override
    public void set(String name, String value) {
        database.set(name, value);
    }

    @Override
    public String get(String name) {
        return database.get(name);
    }

    @Override
    public void unset(String name) {
        database.unset(name);
    }

    @Override
    public Integer numEqualTo(String value) {
        return database.numEqualTo(value);
    }

    @Override
    public void apply(StorageChanges changes) {
        database.apply(changes);
    }
}

/**
 * Default transaction layer. Contains full implementation of:
 * begin, commit, rollback
 * Also works as a proxy with underlying database and keeps local diff.
 */
class DefaultTransactionLayer extends BaseDatabase implements TransactionLayer {

    private TransactionLayer database;

    public DefaultTransactionLayer(TransactionLayer database) {
        super();
        this.database = database;
    }

    @Override
    public TransactionLayer begin() {
        return new DefaultTransactionLayer(this);
    }

    @Override
    public TransactionLayer commit() {
        StorageChanges changes = new StorageChanges(kv, stats);
        database.apply(changes);
        return database.commit();
    }

    @Override
    public TransactionLayer rollback() {
        return database;
    }

    @Override
    public Boolean isComplete() {
        return true;
    }

    @Override
    public String get(String name) {
      if(kv.containsKey(name))
          return kv.get(name);
      else
          return database.get(name); // get from underlying database
    }

    @Override
    public Integer numEqualTo(String value) {
      String key = value;
      if(stats.containsKey(key))
          return stats.get(key);
      else
          return database.numEqualTo(key); // get from underlying storage
    }

    @Override
    void kvRemove(String key) {
        kv.put(key, null);
    }

    @Override
    void statsRemove(String key) {
        stats.put(key, null);
    }

    @Override
    void statsRecordHook(String k) {
        // ensure the stats record is loaded from underlying database
        if(!stats.containsKey(k)) {
            Integer value = database.numEqualTo(k);
            stats.put(k, value);
        }
    }
}

/**
 * Transaction storage changes (diff)
 */
class StorageChanges {
    private HashMap<String, String> kv;
    private HashMap<String, Integer> stats;

    public StorageChanges(HashMap<String, String> kv, HashMap<String, Integer> stats) {
        this.kv = kv;
        this.stats = stats;
    }

    public HashMap<String, String> kv() {
        return kv;
    }

    public HashMap<String, Integer> stats() {
        return stats;
    }
}
