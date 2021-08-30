package org.numamo.lib.test.queue.manager.model;

import org.springframework.jms.core.JmsTemplate;


public final class InputOutputJmsConnection {


    // Variables and constructors:-------------------------------------------------------
    private final String id;
    private final InputOutputConnectionParams params;
    private volatile OutputFixedResponse outputFixedResponse;
    private volatile JmsTemplate jmsTemplate;
    private volatile State state;

    public InputOutputJmsConnection(
            String id,
            InputOutputConnectionParams params,
            OutputFixedResponse outputFixedResponse
    ) {
        this.id = id;
        this.params = params;
        this.outputFixedResponse = outputFixedResponse;
    }


    // Public API:-----------------------------------------------------------------------
    public String getId() {
        return id;
    }

    public InputOutputConnectionParams getParams() {
        return params;
    }

    public OutputFixedResponse getOutputFixedResponse() {
        return outputFixedResponse;
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public State getState() {
        return state;
    }

    public synchronized void setState(State state) {
        this.state = state;
    }

    public synchronized void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
        this.setState(State.CONNECTED);
    }

    /**
     * The connection state model
     */
    enum State {
        NEW,
        READY,
        CONNECTED,
        REMOVED
    }

}
