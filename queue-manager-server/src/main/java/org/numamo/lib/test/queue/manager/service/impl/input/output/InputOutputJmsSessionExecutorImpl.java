package org.numamo.lib.test.queue.manager.service.impl.input.output;


import org.numamo.lib.test.queue.manager.service.api.MessagePrinter;
import org.numamo.lib.test.queue.manager.service.api.input.output.InputOutputJmsSessionExecutor;
import org.numamo.lib.test.queue.manager.service.api.input.output.InputOutputRunnableFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The main executor of the JMS IO session messaging.
 * The executor is based on the ExecutorService thread pool which is started and
 * stopped according to the bean lifecycle
 *
 * @author Nazimov Aleksey I.
 */
public final class InputOutputJmsSessionExecutorImpl implements InputOutputJmsSessionExecutor {


    // Variables and constructors:-------------------------------------------------------
    private static final Logger LOGGER = LoggerFactory.getLogger(InputOutputJmsSessionExecutorImpl.class);

    private final Object mutex = new Object();

    private ExecutorService executor;
    private final int threadNumber;
    private final InputOutputRunnableFactory factory;
    private final MessagePrinter messagePrinter;

    public InputOutputJmsSessionExecutorImpl(
            int threadNumber,
            InputOutputRunnableFactory factory,
            MessagePrinter messagePrinter
    ) {
        this.threadNumber = threadNumber;
        this.factory = factory;
        this.messagePrinter = messagePrinter;
    }

    @PostConstruct
    public void init() {
        executor = Executors.newFixedThreadPool(threadNumber, runnable -> {
            final Thread t = new Thread();
            t.setPriority(Thread.MAX_PRIORITY);
            t.setName("JMS-Executor");
            return t;
        });
        for (int task = 0; task < threadNumber; ++task) {
            executor.execute(factory.make());
        }
        LOGGER.info("IO JMS session executor was launched with {} background threads", threadNumber);
    }

    @PreDestroy
    public void close() {
        jobNotification();
        executor.shutdown();
        executor.shutdownNow();
    }


    // Public API:-----------------------------------------------------------------------
    @Override
    public void parkThread() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("The thread {} is going to be stopped", messagePrinter.printThreadId());
        }
        synchronized (mutex) {
            try {
                mutex.wait();
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("The thread {} was activated by notification", messagePrinter.printThreadId());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("The thread {} was interrupted", messagePrinter.printThreadId());
                }
            }
        }
    }

    @Override
    public void jobNotification() {
        LOGGER.info("New messages! Just wake up");
        synchronized (mutex) {
            mutex.notifyAll();
        }
    }

}
