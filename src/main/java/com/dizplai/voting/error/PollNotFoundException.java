package com.dizplai.voting.error;

public class PollNotFoundException  extends RuntimeException {
    public PollNotFoundException(String id) {
        super("Could not find poll with identifier " + id);
    }
}
