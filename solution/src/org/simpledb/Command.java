package org.simpledb;

/**
 * Base interface for commands
 */
public interface Command {
    Response execute(TransactionLayer database);
}
