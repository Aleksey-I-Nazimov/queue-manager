package org.numamo.lib.test.queue.manager.service.api.input.output;

import org.numamo.lib.test.queue.manager.api.InputOutputMessageCustomizer;
import org.numamo.lib.test.queue.manager.api.InputOutputTextProcessor;
import org.numamo.lib.test.queue.manager.model.InputOutputConnectionParams;
import org.numamo.lib.test.queue.manager.model.OutputFixedResponse;
import org.springframework.jms.core.JmsTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;


public interface InputOutputJmsSessionStorage {


    Predicate<InputOutputJmsSession> NEW_SESSION = s -> InputOutputJmsSession.State.NEW == s.getState();
    Predicate<InputOutputJmsSession> READY_SESSION = s -> InputOutputJmsSession.State.READY == s.getState();
    Predicate<InputOutputJmsSession> CONNECTED_SESSION = s -> InputOutputJmsSession.State.CONNECTED == s.getState();
    Predicate<InputOutputJmsSession> REMOVED_SESSION = s -> InputOutputJmsSession.State.REMOVED == s.getState();


    String makeNewSession(InputOutputConnectionParams connectionParams);


    InputOutputConnectionParams getConnectionParams(String sessionId);


    void setJmsTemplate(
            String sessionId,
            JmsTemplate jmsTemplate
    );


    Optional<JmsTemplate> getJmsTemplate(String sessionId);


    void setFixedResponse(
            String sessionId,
            OutputFixedResponse outputFixedResponse
    );


    void setTextProcessor(
            String sessionId,
            InputOutputTextProcessor textProcessor
    );


    void setMessageCustomizer(
            String sessionId,
            InputOutputMessageCustomizer messageCustomizer
    );


    Optional<InputOutputMessageCustomizer> getMessageCustomizer(String sessionId);


    Optional<InputOutputTextProcessor> getTextProcessor(String sessionId);


    Map<String, InputOutputJmsSession> select(Predicate<InputOutputJmsSession> sessionPredicate);


    void remove(String sessionId);


}
