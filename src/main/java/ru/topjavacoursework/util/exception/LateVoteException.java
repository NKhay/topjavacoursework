package ru.topjavacoursework.util.exception;

import java.time.LocalTime;


public class LateVoteException extends RuntimeException {

    public LateVoteException(LocalTime deadline) {
        super("Can't change vote after " + deadline.toString());
    }

}
