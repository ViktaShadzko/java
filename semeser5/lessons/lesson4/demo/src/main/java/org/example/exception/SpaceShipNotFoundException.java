package org.example.exception;

public class SpaceShipNotFoundException extends RuntimeException     {
    public SpaceShipNotFoundException(String s) {
        super(s);
    }
}
