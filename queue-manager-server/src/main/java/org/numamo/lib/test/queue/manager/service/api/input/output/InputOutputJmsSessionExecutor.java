package org.numamo.lib.test.queue.manager.service.api.input.output;

public interface InputOutputJmsSessionExecutor {

    void parkThread();

    void jobNotification();

}
