package org.numamo.lib.test.queue.manager.service.impl.input.output;

import org.numamo.lib.test.queue.manager.service.api.input.output.InputOutputInfoCollector;
import org.numamo.lib.test.queue.manager.service.api.input.output.InputOutputJmsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * The class of the main processing data collector
 *
 * @author Nazimov Aleksey I.
 */
public final class InputOutputInfoCollectorImpl implements InputOutputInfoCollector {


    // Variables and constructors:-------------------------------------------------------
    private static final Logger LOGGER = LoggerFactory.getLogger(InputOutputInfoCollectorImpl.class);

    private final Map<String, AtomicInteger> messageNumber = new ConcurrentHashMap<>();
    private final Map<String, Map<String, EmptyStatus>> emptyStat = new HashMap<>();
    private final Map<String, List<String>> unknownMessageTypes = new HashMap<>();
    private final Map<String, List<Exception>> errorStat = new HashMap<>();


    // Public API:-----------------------------------------------------------------------
    @Override
    public void registerInputOutputMessages(
            final InputOutputJmsSession session,
            final String inputMessage,
            final String outputMessage
    ) {
        LOGGER.trace("The new IO messages:\n{}\n{}", inputMessage, outputMessage);
        messageNumber
                .computeIfAbsent(session.getSessionId(), i -> new AtomicInteger())
                .addAndGet(1);
    }

    @Override
    public void registerEmptyInputQueueState(
            final String threadId,
            final InputOutputJmsSession session,
            final boolean empty
    ) {
        LOGGER.debug("The session {} is partially empty with in {}", session, threadId);
        emptyStat.computeIfAbsent(
                session.getSessionId(),
                i -> new ConcurrentHashMap<>()
        )
                .put(threadId, new EmptyStatus(empty));
    }

    @Override
    public void registerUnknownMessageType(
            final InputOutputJmsSession session,
            final String messageType
    ) {
        LOGGER.warn("Unknown message type: {} with in {}", messageType, session);
        unknownMessageTypes.computeIfAbsent(
                session.getSessionId(),
                i -> new CopyOnWriteArrayList<>()
        )
                .add(messageType);
    }

    @Override
    public void registerError(
            InputOutputJmsSession session,
            Exception e
    ) {
        LOGGER.trace("Registering error...");
        errorStat.computeIfAbsent(
                session.getSessionId(),
                i -> new CopyOnWriteArrayList<>()
        )
                .add(e);
    }


    /**
     * Empty status data class
     */
    private static class EmptyStatus {

        private final Date date;
        private final boolean empty;

        public EmptyStatus(boolean empty) {
            this.date = new Date();
            this.empty = empty;
        }

        public Date getDate() {
            return date;
        }

        public boolean isEmpty() {
            return empty;
        }

        @Override
        public String toString() {
            return "EmptyStatus{" +
                    "date=" + date +
                    ", empty=" + empty +
                    '}';
        }
    }
}
