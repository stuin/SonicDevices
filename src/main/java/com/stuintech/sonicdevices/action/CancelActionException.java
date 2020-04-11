package com.stuintech.sonicdevices.action;

public class CancelActionException extends Exception {
    public CancelActionException() {
        super("No action should be taken");
    }
}
