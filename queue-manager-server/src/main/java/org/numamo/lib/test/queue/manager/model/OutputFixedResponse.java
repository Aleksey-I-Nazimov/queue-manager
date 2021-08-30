package org.numamo.lib.test.queue.manager.model;


public final class OutputFixedResponse {

    private final String message;

    public OutputFixedResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "OutputFixedResponse{" +
                "message='" + message + '\'' +
                '}';
    }

}
