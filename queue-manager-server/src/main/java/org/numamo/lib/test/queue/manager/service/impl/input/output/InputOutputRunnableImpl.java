package org.numamo.lib.test.queue.manager.service.impl.input.output;


import org.numamo.lib.test.queue.manager.api.InputOutputTextProcessor;
import org.numamo.lib.test.queue.manager.model.InputOutputConnectionParams;
import org.numamo.lib.test.queue.manager.service.api.MessagePrinter;
import org.numamo.lib.test.queue.manager.service.api.input.output.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.IllegalStateException;
import javax.jms.*;
import java.util.Map;

import static java.lang.Thread.currentThread;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.Objects.nonNull;
import static org.numamo.lib.test.queue.manager.service.api.input.output.InputOutputJmsSessionStorage.CONNECTED_SESSION;


/**
 * The class implements Runnable interface and makes the background
 * action of the INPUT/OUTPUT messaging process
 *
 * @author Nazimov Aleksey I.
 */
public final class InputOutputRunnableImpl implements Runnable {


    // Variables and constructors:-------------------------------------------------------
    private static final Logger LOGGER = LoggerFactory.getLogger(InputOutputRunnableImpl.class);

    private final InputOutputJmsSessionStorage jmsSessionStorage;
    private final InputOutputJmsMessageFactory jmsMessageFactory;
    private final InputOutputJmsSessionExecutor jmsSessionExecutor;
    private final InputOutputInfoCollector infoCollector;
    private final MessagePrinter messagePrinter;

    public InputOutputRunnableImpl(
            InputOutputJmsSessionStorage jmsSessionStorage,
            InputOutputJmsMessageFactory jmsMessageFactory,
            InputOutputJmsSessionExecutor jmsSessionExecutor,
            InputOutputInfoCollector infoCollector,
            MessagePrinter messagePrinter
    ) {
        this.jmsSessionStorage = jmsSessionStorage;
        this.jmsMessageFactory = jmsMessageFactory;
        this.jmsSessionExecutor = jmsSessionExecutor;
        this.infoCollector = infoCollector;
        this.messagePrinter = messagePrinter;
    }


    // Public API:-----------------------------------------------------------------------
    @Override
    public void run() {

        while (!currentThread().isInterrupted()) {

            final Map<String, InputOutputJmsSession> sessionMap = jmsSessionStorage.select(CONNECTED_SESSION);
            sessionMap.forEach(
                    (sessionId, session) -> {
                        try {
                            if (LOGGER.isTraceEnabled()) {
                                LOGGER.trace("IO thread {} => IO messaging", messagePrinter.printThreadId());
                            }

                            makeInputOutputMessaging(session);

                        } catch (Exception e) {
                            LOGGER.error("The IO messaging error: cause->", e);
                            infoCollector.registerError(session, e);
                        }
                    }
            );
            if (sessionMap.isEmpty()) {
                jmsSessionExecutor.parkThread();
            }
        }
    }

    private void makeInputOutputMessaging(InputOutputJmsSession session) throws JMSException {
        LOGGER.trace("IO messaging for session {}", session);
        final JmsTemplate jmsTemplate = session.getJmsTemplate();
        final InputOutputConnectionParams params = session.getConnectionParams();
        final InputOutputTextProcessor textProcessor = session.getTextProcessor();

        final Message inputMessage = jmsTemplate.receive(params.getConnectedInputQueue());

        if (nonNull(inputMessage)) {
            final String request = readFromMessage(inputMessage, session);
            final String fullRequest = messagePrinter.print(inputMessage);
            final String response = textProcessor.makeFromInput(request);

            jmsTemplate.send(
                    params.getConnectedOutputQueue(),
                    s -> {
                        final Message outputMessage = jmsMessageFactory.make(s, session, response);
                        final String fullResponse = messagePrinter.print(outputMessage);
                        infoCollector.registerInputOutputMessages(session, fullRequest, fullResponse);
                        return outputMessage;
                    }
            );
        } else {
            infoCollector.registerEmptyInputQueueState(messagePrinter.printThreadId(), session);
        }
    }

    private String readFromMessage(Message message, InputOutputJmsSession session) throws JMSException {

        if (message instanceof BytesMessage) {
            final BytesMessage bytesMessage = (BytesMessage) message;
            final byte[] bytes = new byte[(int) bytesMessage.getBodyLength()];
            if (bytesMessage.readBytes(bytes) != bytes.length) {
                throw new IllegalStateException("The message cannot be read correctly: " + message);
            }
            LOGGER.trace("The input bytes message was read successfully");
            return new String(bytes, defaultCharset());

        } else if (message instanceof TextMessage) {
            LOGGER.trace("The output bytes message was read successfully");
            return ((TextMessage) message).getText();

        } else {
            if (message != null) {
                // It means unknown message
                infoCollector.registerUnknownMessageType(session, messagePrinter.printClassType(message));
            }
            // It means no suitable message for reading
            return null;
        }
    }

}
