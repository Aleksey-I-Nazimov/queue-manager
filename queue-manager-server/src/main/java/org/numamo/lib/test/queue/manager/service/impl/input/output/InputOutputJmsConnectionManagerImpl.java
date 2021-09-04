package org.numamo.lib.test.queue.manager.service.impl.input.output;

import org.numamo.lib.test.queue.manager.service.api.input.output.InputOutputJmsConnectionManager;
import org.numamo.lib.test.queue.manager.service.api.input.output.InputOutputJmsProvider;
import org.numamo.lib.test.queue.manager.service.api.input.output.InputOutputJmsSession;
import org.numamo.lib.test.queue.manager.service.api.input.output.InputOutputJmsSessionStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public final class InputOutputJmsConnectionManagerImpl implements InputOutputJmsConnectionManager {


    // Variables and constructors:-------------------------------------------------------
    private static final Logger LOGGER = LoggerFactory.getLogger(InputOutputJmsConnectionManagerImpl.class);

    private final InputOutputJmsProvider jmsProvider;
    private final InputOutputJmsSessionStorage sessionStorage;

    public InputOutputJmsConnectionManagerImpl(
            InputOutputJmsProvider jmsProvider,
            InputOutputJmsSessionStorage sessionStorage
    ) {
        this.jmsProvider = jmsProvider;
        this.sessionStorage = sessionStorage;
    }


    // Public API:-----------------------------------------------------------------------
    @Override
    public synchronized void makeJmsTemplateForAllReadySessions() {

        final Map<String, InputOutputJmsSession> sessionMap = sessionStorage
                .select(InputOutputJmsSessionStorage.READY_SESSION);

        sessionMap
                .forEach(
                        (sessionId, session) -> {
                            session.setJmsTemplate(jmsProvider.get(session.getConnectionParams()));
                        }
                );

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("JMS template was generated for {} sessions", sessionMap.size());
        }
    }
}
