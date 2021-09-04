package org.numamo.lib.test.queue.manager.service.api.input.output;

public interface InputOutputInfoCollector {

    void registerInputOutputMessages(
            InputOutputJmsSession session,
            String inputMessage,
            String outputMessage
    );

    void registerEmptyInputQueueState(
            String threadId,
            InputOutputJmsSession session,
            boolean empty
    );

    void registerUnknownMessageType(
            InputOutputJmsSession session,
            String messageType
    );

    void registerError(
            InputOutputJmsSession session,
            Exception e
    );

}
