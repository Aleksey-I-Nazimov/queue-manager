package org.numamo.lib.test.queue.manager.service.api;


import org.numamo.lib.test.queue.manager.model.InputOutputConnectionParams;
import org.springframework.jms.core.JmsTemplate;


public interface InputOutputJmsProvider {

    JmsTemplate get (InputOutputConnectionParams inputOutputConnectionParams);

}
