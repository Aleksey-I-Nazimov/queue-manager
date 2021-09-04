package org.numamo.lib.test.queue.manager.service.api.exception;


/**
 * The exception is thrown when the sesion was not found with required ID
 *
 * @author Nazimov Aleksey I.
 */
public class SessionNotFoundException extends RuntimeException {

    private final String sessionId;

    public SessionNotFoundException(String sessionId) {
        super("The session with ID=" + sessionId + " was not found");
        this.sessionId = sessionId;
    }

}
