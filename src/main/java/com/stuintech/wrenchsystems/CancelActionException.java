package com.stuintech.wrenchsystems;

public class CancelActionException extends Exception {
    public CancelActionException() {
        super("No action should be taken");
    }
}
