package com.stuintech.sonicdevicesapi;

public class CancelActionException extends Exception {
    public CancelActionException() {
        super("No action should be taken");
    }
}
