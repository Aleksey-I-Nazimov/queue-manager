package org.numamo.lib.test.queue.manager.api;

import javax.jms.Message;


public interface InputOutputMessageCustomizer {

    void apply (Message inputMessage, Message preparedOutputMessage);

}
