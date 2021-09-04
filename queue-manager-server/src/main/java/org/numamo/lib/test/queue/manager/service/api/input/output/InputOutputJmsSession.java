package org.numamo.lib.test.queue.manager.service.api.input.output;

import org.numamo.lib.test.queue.manager.api.InputOutputMessageCustomizer;
import org.numamo.lib.test.queue.manager.api.InputOutputTextProcessor;
import org.numamo.lib.test.queue.manager.model.InputOutputConnectionParams;
import org.numamo.lib.test.queue.manager.model.OutputFixedResponse;
import org.springframework.jms.core.JmsTemplate;

import java.util.Optional;


/**
 * The interface of the so-called JMS session. It includes the respective broker connection
 * parameters and text processor of transmitted messages.
 * This session has a JmsTemplate object.
 *
 * @author Nazimov Aleksey I.
 */
public interface InputOutputJmsSession {


    /**
     * The INPUT-OUTPUT session model
     */
    enum State {
        NEW,
        READY,
        CONNECTED,
        REMOVED
    }


    /**
     * The method gets the session ID
     *
     * @return the string of session ID
     */
    String getSessionId();


    /**
     * The method gets the basic JMS session parameters used
     * to make connection to the JMS broker
     *
     * @return the basic JMS parameters
     */
    InputOutputConnectionParams getConnectionParams();


    /**
     * The JMS template setter
     *
     * @param jmsTemplate the new JMS template
     */
    void setJmsTemplate(JmsTemplate jmsTemplate);


    /**
     * The method gets the JMS template
     *
     * @return the reference of JMS template
     */
    JmsTemplate getJmsTemplate();


    /**
     * The method sets the string response provider which is converted to the text processor
     *
     * @param outputFixedResponse is the string response provider
     */
    void setFixedResponse(OutputFixedResponse outputFixedResponse);

    /**
     * The method sets the string text processor used for the IO text processing
     *
     * @param textProcessor is the text processor
     */
    void setTextProcessor(InputOutputTextProcessor textProcessor);


    /**
     * The method gets the internal text processor which can be represented by the
     * string response provider or text processor
     *
     * @return the internal text processor
     */
    InputOutputTextProcessor getTextProcessor();


    /**
     * The method sets the internal message customizer
     *
     * @param messageCustomizer is the JMS message customizer
     */
    void setMessageCustomizer(InputOutputMessageCustomizer messageCustomizer);


    /**
     * The method gets the internal message customizer
     *
     * @return the internal JMS message customizer
     */
    Optional<InputOutputMessageCustomizer> getMessageCustomizer();


    /**
     * The gets the current session state.
     * The internal state can be changed by setting the main setting objects and parameters like JmsTemplate
     *
     * @return the session state
     */
    State getState();


    /**
     * The method marks this session as a removed one
     */
    void disable();


}
