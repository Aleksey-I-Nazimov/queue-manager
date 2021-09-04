package org.numamo.lib.test.queue.manager.service.api.input.output;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public interface InputOutputJmsMessageFactory {

    Message make(
            Session liveJmsSession,
            InputOutputJmsSession ioSession,
            String messageBody
    ) throws JMSException;

}
