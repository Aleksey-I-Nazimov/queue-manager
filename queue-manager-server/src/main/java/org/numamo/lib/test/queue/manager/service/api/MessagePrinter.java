package org.numamo.lib.test.queue.manager.service.api;

import javax.jms.Message;

import static java.lang.Thread.currentThread;

public interface MessagePrinter {

    String print(Message message);

    String printClassType(Object object);

    default String printThreadId() {
        final Thread thread = currentThread();
        return "TID:" + thread.getName() + thread.getId();
    }

}
