package org.numamo.lib.test.queue.manager.service.impl.input.output;

import org.numamo.lib.test.queue.manager.api.InputOutputMessageCustomizer;
import org.numamo.lib.test.queue.manager.api.InputOutputTextProcessor;
import org.numamo.lib.test.queue.manager.model.InputOutputConnectionParams;
import org.numamo.lib.test.queue.manager.model.OutputFixedResponse;
import org.numamo.lib.test.queue.manager.service.api.IdGenerator;
import org.numamo.lib.test.queue.manager.service.api.exception.SessionNotFoundException;
import org.numamo.lib.test.queue.manager.service.api.input.output.InputOutputJmsSession;
import org.numamo.lib.test.queue.manager.service.api.input.output.InputOutputJmsSessionStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import static java.util.Objects.*;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;


/**
 * The session manager.
 * This manager is constructed as a hash map. All internal sessions
 * have a key and stored with in the hash table. The manager API allows to make
 * some actions with the stored sessions
 *
 * @author Nazimov Aleksey I.
 */
public final class InputOutputJmsSessionStorageImpl implements InputOutputJmsSessionStorage {


    // Variables and constructors:-------------------------------------------------------
    private static final Logger LOGGER = LoggerFactory.getLogger(InputOutputJmsSessionStorageImpl.class);

    private final Map<String, InputOutputJmsSession> hashTable = new ConcurrentHashMap<>();

    private final IdGenerator idGenerator;

    public InputOutputJmsSessionStorageImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }


    // Public API:-----------------------------------------------------------------------
    @Override
    public synchronized String makeNewSession(
            final InputOutputConnectionParams connectionParams
    ) {
        String id = idGenerator.next();
        while (hashTable.containsKey(id)) {
            id = idGenerator.next();
        }
        final InputOutputJmsSession session = new InputOutputJmsSessionImpl(id, connectionParams);
        final InputOutputJmsSession previous = hashTable.put(session.getSessionId(), session);
        if (nonNull(previous)) {
            // Self -check:
            throw new IllegalStateException("The collision by session key=" + id);
        }
        LOGGER.debug("The new session was created {}", session);
        return id;
    }


    @Override
    public InputOutputConnectionParams getConnectionParams(
            final String sessionId
    ) {
        LOGGER.debug("Requesting session params for the session with ID={}", sessionId);
        return getOrError(sessionId).getConnectionParams();
    }


    @Override
    public void setJmsTemplate(
            final String sessionId,
            final JmsTemplate jmsTemplate
    ) {

        final InputOutputJmsSession session = getOrError(sessionId);
        session.setJmsTemplate(requireNonNull(jmsTemplate));

        LOGGER.debug("Setting JMS template for the session = {}", session);
    }


    @Override
    public Optional<JmsTemplate> getJmsTemplate(
            final String sessionId
    ) {
        LOGGER.debug("Requesting JMS template by ID = {}", sessionId);
        final InputOutputJmsSession session = getOrError(sessionId);
        return of(session.getJmsTemplate());
    }


    @Override
    public void setFixedResponse(
            final String sessionId,
            final OutputFixedResponse outputFixedResponse
    ) {
        LOGGER.debug("Setting the fixed string responder for session ID = {}", sessionId);
        final InputOutputJmsSession session = getOrError(sessionId);
        session.setFixedResponse(outputFixedResponse);
    }


    @Override
    public void setTextProcessor(
            final String sessionId,
            final InputOutputTextProcessor textProcessor
    ) {
        LOGGER.debug("Setting the text processor for session ID = {}", sessionId);
        final InputOutputJmsSession session = getOrError(sessionId);
        session.setTextProcessor(textProcessor);
    }


    @Override
    public void setMessageCustomizer(
            final String sessionId,
            final InputOutputMessageCustomizer messageCustomizer
    ) {
        LOGGER.debug("Setting the message customizer for session ID = {}", sessionId);
        final InputOutputJmsSession session = getOrError(sessionId);
        session.setMessageCustomizer(messageCustomizer);
    }


    @Override
    public Optional<InputOutputMessageCustomizer> getMessageCustomizer(
            final String sessionId
    ) {
        LOGGER.debug("Requesting message customizer for session ID = {}", sessionId);
        final InputOutputJmsSession session = getOrError(sessionId);
        return session.getMessageCustomizer();
    }


    @Override
    public Optional<InputOutputTextProcessor> getTextProcessor(
            final String sessionId
    ) {
        LOGGER.debug("Requesting text processor for session ID = {}", sessionId);
        return ofNullable(getOrError(sessionId).getTextProcessor());
    }

    @Override
    public Map<String, InputOutputJmsSession> select(
            final Predicate<InputOutputJmsSession> sessionPredicate
    ) {
        final Map<String, InputOutputJmsSession> selected = new HashMap<>();
        for (final Map.Entry<String, InputOutputJmsSession> entry : hashTable.entrySet()) {
            if (sessionPredicate.test(entry.getValue())) {
                selected.put(entry.getKey(), entry.getValue());
            }
        }
        return Collections.unmodifiableMap(selected);
    }

    @Override
    public synchronized void remove(String sessionId) {
        final InputgetOrError(sessionId).disable();

        for (final Map.Entry<String, InputOutputJmsSession> entry : hashTable.entrySet()) {

            entry.getValue().d;
            if (sessionPredicate.test(entry.getValue())) {
                selected.put(entry.getKey(), entry.getValue());
            }
        }
    }


    // Internal methods:-----------------------------------------------------------------
    private InputOutputJmsSession getOrError(String sessionId) {
        final InputOutputJmsSession session = hashTable.get(sessionId);
        if (isNull(session)) {
            throw new SessionNotFoundException(sessionId);
        } else {
            return session;
        }
    }

}
