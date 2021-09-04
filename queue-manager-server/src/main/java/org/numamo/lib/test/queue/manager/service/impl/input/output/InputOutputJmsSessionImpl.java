package org.numamo.lib.test.queue.manager.service.impl.input.output;

import org.numamo.lib.test.queue.manager.api.InputOutputMessageCustomizer;
import org.numamo.lib.test.queue.manager.api.InputOutputTextProcessor;
import org.numamo.lib.test.queue.manager.model.InputOutputConnectionParams;
import org.numamo.lib.test.queue.manager.model.OutputFixedResponse;
import org.numamo.lib.test.queue.manager.service.api.input.output.InputOutputJmsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;
import static org.numamo.lib.test.queue.manager.service.api.input.output.InputOutputJmsSession.State.REMOVED;


/**
 * The INPUT / OUTPUT session.
 *
 * @author Nazimov Aleksey I.
 */
public final class InputOutputJmsSessionImpl implements InputOutputJmsSession {


    // Variables and constructors:-------------------------------------------------------
    private static final Logger LOGGER = LoggerFactory.getLogger(InputOutputJmsSessionImpl.class);

    private final String id;
    private final InputOutputConnectionParams connectionParams;

    private volatile JmsTemplate jmsTemplate;

    private volatile InputOutputMessageCustomizer messageCustomizer =
            (i, p) -> LOGGER.info("The default message customizer makes nothing");

    private volatile InputOutputTextProcessor textProcessor;
    private volatile State state = State.NEW;

    public InputOutputJmsSessionImpl(
            String id,
            InputOutputConnectionParams connectionParams
    ) {
        this.id = requireNonNull(id);
        this.connectionParams = requireNonNull(connectionParams);
    }


    // Public API:-----------------------------------------------------------------------
    @Override
    public String getSessionId() {
        LOGGER.trace("[ID={}] Requesting session ID = {}", id, id);
        return id;
    }

    @Override
    public InputOutputConnectionParams getConnectionParams() {
        LOGGER.trace("[ID={}] Getting connection params {}", id, connectionParams);
        return connectionParams;
    }

    @Override
    public synchronized void setJmsTemplate(JmsTemplate jmsTemplate) {
        LOGGER.trace("[ID={}] Setting the new JMS template {}", id, jmsTemplate);
        this.jmsTemplate = jmsTemplate;
        this.state = State.CONNECTED;
    }

    @Override
    public synchronized JmsTemplate getJmsTemplate() {
        LOGGER.trace("[ID={}] Getting the jms template= {}", id, jmsTemplate);
        return jmsTemplate;
    }


    @Override
    public synchronized void setFixedResponse(
            OutputFixedResponse outputFixedResponse
    ) {
        LOGGER.trace("[ID={}] Setting the new fixed response {}", id, outputFixedResponse);
        this.textProcessor = inputQueueText -> outputFixedResponse.getMessage();
        this.state = State.READY;
    }

    @Override
    public synchronized void setTextProcessor(
            InputOutputTextProcessor textProcessor
    ) {
        LOGGER.trace("[ID={}] Setting text processor {}", id, textProcessor);
        this.textProcessor = textProcessor;
        this.state = State.READY;
    }

    @Override
    public synchronized InputOutputTextProcessor getTextProcessor() {
        LOGGER.trace("[ID={}] Getting text processor {}", id, textProcessor);
        return textProcessor;
    }

    @Override
    public synchronized void setMessageCustomizer(
            InputOutputMessageCustomizer messageCustomizer
    ) {
        LOGGER.trace("[ID={}] Setting the new message customizer {}", id, messageCustomizer);
        this.messageCustomizer = requireNonNull(messageCustomizer);
    }

    @Override
    public synchronized Optional<InputOutputMessageCustomizer> getMessageCustomizer() {
        LOGGER.trace("[ID={}] Getting the new message customizer {}", id, messageCustomizer);
        return of(messageCustomizer);
    }

    @Override
    public synchronized State getState() {
        LOGGER.trace("[ID={}] Getting the state {}", id, state);
        return state;
    }

    @Override
    public synchronized void disable() {
        LOGGER.debug("[ID={}] Marking this session as a removed", id);
        this.state = REMOVED;
    }

    @Override
    public String toString() {
        return "InputOutputJmsSessionImpl{" +
                "id='" + id + '\'' +
                ", connectionParams=" + connectionParams +
                ", state=" + state +
                '}';
    }
}
