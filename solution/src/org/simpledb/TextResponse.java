package org.simpledb;

/**
 * Text response
 */
public final class TextResponse extends BaseResponse implements Response {
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
