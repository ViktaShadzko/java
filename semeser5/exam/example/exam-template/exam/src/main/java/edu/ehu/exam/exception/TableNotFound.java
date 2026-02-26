package edu.ehu.exam.exception;

public class TableNotFound extends RuntimeException {
    public TableNotFound(String message) {
        super(message);
    }
}
