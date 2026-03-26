package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class Exception extends java.lang.Exception {
    public Exception(String message) {
        super(message);
    }
    public Exception(String message, Throwable ex) {
        super(message, ex);
    }
}
