package com.stuintech.sonicdevices.actions;

public class CancelActionException extends Exception {
    public CancelActionException() {
        super("No action should be taken");
    }
}
