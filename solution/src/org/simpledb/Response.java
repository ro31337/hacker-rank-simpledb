package org.simpledb;

/**
 * Command response
 */
public interface Response {
    /**
     * String representation of response
     */
    String toString();

    /**
     * Database with the new state
     */
    TransactionLayer database();
}
